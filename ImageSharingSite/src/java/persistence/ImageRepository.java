package persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageRepository {

    private static ImageRepository INSTANCE;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final ExecutorService ioExecutor = Executors.newCachedThreadPool(new IOThreadFactory());

    private final Map<String, byte[]> imageCache = new ConcurrentHashMap<>();

    private final Map<String, Integer> cachingTreshold = new ConcurrentHashMap<>();

    private final ArrayBlockingQueue<DataPair> ioQueue = new ArrayBlockingQueue<>(200, true);

    private int decrementValue = 5;

    private int purgeValue = 0;

    private int incrementValue = 1;

    private int ioTasksPerCycle = 50;

    private final Path basePath = Paths.get("imgrepo");

    private ImageRepository() {
	scheduler.scheduleAtFixedRate(() -> {
	    ArrayList<String> thingsToDelete = new ArrayList();
	    cachingTreshold.forEach((key, value) -> {
		if (!isQueued(key)) {
		    cachingTreshold.put(key, value - decrementValue);
		    if (cachingTreshold.get(key) <= purgeValue) {
			thingsToDelete.add(key);
		    }
		}
	    });

	    thingsToDelete.forEach(cachingTreshold::remove);

	    for (int i = 0; i < Math.min(ioTasksPerCycle, ioQueue.size()); i++) {
		ioExecutor.execute(() -> {
		    DataPair pair = ioQueue.poll();
		    try {
			Files.write(basePath.resolve(pair.getKey() + ".dat"), pair.getData(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		    } catch (IOException ex) {

		    }
		});
	    }

	}, 120, 30, TimeUnit.SECONDS);
    }

    private boolean isQueued(String key) {
	return ioQueue.stream().anyMatch((pair) -> pair.getKey().equals(key));
    }

    public void addRequest(String image) {
	cachingTreshold.putIfAbsent(image, 0);
	cachingTreshold.put(image, cachingTreshold.get(image) + incrementValue);
    }

    public byte[] getImage(String image) {
	return imageCache.getOrDefault(this, null/*TODO: Add loading from disk */);
    }

    public void saveImage(String key, byte[] imageData) {
	imageCache.put(key, imageData);
	ioQueue.add(new DataPair(key, imageData));
    }

    public static ImageRepository getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new ImageRepository();
	}
	return INSTANCE;
    }

    private class IOThreadFactory implements ThreadFactory {

	private final AtomicInteger counter = new AtomicInteger();

	@Override
	public Thread newThread(Runnable r) {
	    Thread t = new Thread(r);
	    t.setName("I/O Thread " + counter.getAndIncrement());
	    t.setDaemon(true);
	    return t;
	}

    }

    private class DataPair {

	private String key;
	private byte[] data;

	public DataPair(String key, byte[] data) {
	    this.key = key;
	    this.data = data;
	}

	public String getKey() {
	    return key;
	}

	public byte[] getData() {
	    return data;
	}

    }

    public int getDecrementValue() {
	return decrementValue;
    }

    public void setDecrementValue(int decrementValue) {
	this.decrementValue = decrementValue;
    }

    public int getPurgeValue() {
	return purgeValue;
    }

    public void setPurgeValue(int purgeValue) {
	this.purgeValue = purgeValue;
    }

    public int getIncrementValue() {
	return incrementValue;
    }

    public void setIncrementValue(int incrementValue) {
	this.incrementValue = incrementValue;
    }

}
