package json;

import domain.ImageDescription;
import domain.User;
import domain.Visibility;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.json.JsonWriter;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Frederik
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class UserWriter implements MessageBodyWriter<User> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
	return User.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(User t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
	return -1;
    }

    @Override
    public void writeTo(User user, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
	try (JsonWriter writer = Json.createWriter(entityStream)) {
	    writer.write(WriterUtil.createUser(user));
	}
    }
}
