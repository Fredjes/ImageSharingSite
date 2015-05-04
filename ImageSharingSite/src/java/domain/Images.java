package domain;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import persistence.ImageRepository;

@Path("img")
public class Images {

    @POST
    @Consumes({"image/jpeg", "image/png"})
    public Response testPost(InputStream in, @HeaderParam("Content-Type") String fileType, @HeaderParam("Content-Length") long fileSize) {
	try {
	    String type = "";
	    byte[] b = new byte[(int) fileSize];
	    int by = 0;
	    int pos = 0;
	    while ((by = in.read()) != -1) {
		b[pos++] = (byte) by;
	    }
	    if (fileType.equals("image/jpeg")) {
		type = "jpg";
	    } else {
		type = "png";
	    }
	    return Response.created(URI.create("/" + ImageRepository.getInstance().saveImage(b, type))).build();
	} catch (IOException ex) {
	}

	return Response.serverError().build();
    }

    @Path("{imgId}")
    @GET
    @Produces({"image/jpeg", "image/png"})
    public Response get(@PathParam("imgId") String key) {
	byte[] out = ImageRepository.getInstance().getImage(key);
	if (out == null) {
	    return Response.status(404).build();
	}
	return Response.ok(out).build();
    }
}
