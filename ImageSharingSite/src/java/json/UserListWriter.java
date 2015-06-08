package json;

import domain.User;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.json.JsonWriter;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.json.Json;
import javax.json.JsonArrayBuilder;

/**
 *
 * @author Frederik
 */
public class UserListWriter implements MessageBodyWriter<List<User>> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
	if (!List.class.isAssignableFrom(type)) {
	    return false;
	}

	if (genericType instanceof ParameterizedType) {
	    Type[] arguments = ((ParameterizedType) genericType).getActualTypeArguments();
	    return arguments.length == 1 && arguments[0].equals(User.class);
	} else {
	    return false;
	}
    }

    @Override
    public long getSize(List<User> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
	return -1;
    }

    @Override
    public void writeTo(List<User> userList, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
	try (JsonWriter writer = Json.createWriter(entityStream)) {
	    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	    userList.forEach((User user) -> user.setPrivilege(User.UserPrivilege.PUBLIC));
	    userList.stream().map(WriterUtil::createUser).forEach(arrayBuilder::add);
	    writer.write(arrayBuilder.build());
	}
    }
}
