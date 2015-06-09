package json;

import domain.User;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.json.JsonReader;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Frederik
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class UserReader implements MessageBodyReader<User> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
	return mediaType == MediaType.APPLICATION_JSON_TYPE && type.isAssignableFrom(User.class);
    }

    @Override
    public User readFrom(Class<User> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
	User user = new User();

	try (JsonReader reader = Json.createReader(entityStream)) {
	    JsonObject jsonObject = reader.readObject();

	    user.setUserName(jsonObject.getString("username", null));
	    user.setEmail(jsonObject.getString("email", null));
	    user.setPassword(jsonObject.getString("password", null));
	}

	return user;
    }

}
