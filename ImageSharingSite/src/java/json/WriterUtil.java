package json;

import domain.ImageDescription;
import domain.User;
import domain.Visibility;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;

/**
 *
 * @author Frederik
 */
public class WriterUtil {

    public static JsonStructure createUser(User user) {
	JsonObjectBuilder jsonUser = Json.createObjectBuilder();
	if (user != null) {
	    jsonUser.add("profile-picture-id", user.getProfileImage() == null ? "" : user.getProfileImage().getId());
	    jsonUser.add("username", user.getUserName());
	    jsonUser.add("score", user.getScore());

	    if (user.getPrivilege() == User.UserPrivilege.RESTRICTED) {
		jsonUser.add("email", user.getEmail());
	    }

	    JsonArrayBuilder jsonPictureArray = Json.createArrayBuilder();
	    user.getUploadedImages().stream().filter(i -> i.getVisibility() != Visibility.PRIVATE || user.getPrivilege() == User.UserPrivilege.RESTRICTED).map(ImageDescription::getId).forEach(jsonPictureArray::add);
	    jsonUser.add("uploaded-images", jsonPictureArray);
	}
	return jsonUser.build();
    }
}
