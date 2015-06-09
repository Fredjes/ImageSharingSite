package domain;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Produces({"image/jpeg", "image/png"})
public class ImageWriter implements MessageBodyWriter<byte[]> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
	if (!byte[].class.isAssignableFrom(type)) {
	    return false;
	}

	return true;
    }

    @Override
    public long getSize(byte[] t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
	return -1;
    }

    @Override
    public void writeTo(byte[] t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
	if (t == null) {
	    entityStream.write("Image not found".getBytes());
	    return;
	}
	entityStream.write(t);
    }

}
