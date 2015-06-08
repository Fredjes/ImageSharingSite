package domain;

import domain.validation.ValidPassword;
import domain.validation.groups.OnUserAdd;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Frederik
 */
@Entity
@Table(name = "TBL_USER")
@SecondaryTable(name = "USER_PASSWORD")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
})
public class User implements Serializable {

    public static enum UserPrivilege {

	PUBLIC, RESTRICTED
    }

    @Id
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9_\\-\\.]+")
    private String userName;

    @NotNull
    @Pattern(regexp = "^([\\w\\.\\-_]+)?\\w+@[\\w-_]+(\\.\\w+){1,}$")
    private String email;

    @Transient
    @ValidPassword(message = "Password is invalid", groups = OnUserAdd.class)
    private String password;

    @Column(name = "PASSWORD", table = "USER_PASSWORD")
    private String passwordHash;

    @ElementCollection
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USERNAME"))
    @Column(name = "ROLES")
    private List<String> roles;

    @Min(0)
    private int score;

    @OneToOne(mappedBy = "owner")
    private ImageDescription profileImage;

    @OneToMany(mappedBy = "owner")
    private List<ImageDescription> uploadedImages = new ArrayList<>();

    @Transient
    private UserPrivilege privilage;

    public User() {
    }

    public User(String userName, String email, String password, int score) {
	this.userName = userName;
	this.email = email;
	this.password = password;
	this.score = score;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getPassword() {
	return password;
    }

    public UserPrivilege getPrivilege() {
	return privilage;
    }

    public void setPrivilege(UserPrivilege privilage) {
	this.privilage = privilage;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPasswordHash() {
	return password;
    }

    public void setPasswordHash(String passwordHash) {
	this.passwordHash = passwordHash;
    }

    public static String createPasswordHash(String password) {
	try {
            BigInteger hash = new BigInteger(1, MessageDigest.getInstance("SHA-256")
                    .digest(password.getBytes("UTF-8")));
            return hash.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, "Cannot create password hash", ex);
        }
	
	return "";
    }

    public int getScore() {
	return score;
    }

    public void setScore(int score) {
	this.score = score;
    }

    public List<ImageDescription> getUploadedImages() {
	return uploadedImages;
    }

    public void setUploadedImages(List<ImageDescription> uploadedImages) {
	this.uploadedImages = uploadedImages;
    }

    public ImageDescription getProfileImage() {
	return profileImage;
    }

    public void setProfileImage(ImageDescription profileImage) {
	this.profileImage = profileImage;
    }
}
