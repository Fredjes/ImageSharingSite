package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Frederik
 */
@Entity
@Table(name = "TBL_USER")
public class User implements Serializable {

    @Id
    private String userName;
    private String email;
    private String passwordHash;
    private int score;

    @OneToMany(mappedBy = "owner")
    private List<ImageDescription> uploadedImages = new ArrayList<>();

    public User() {
    }

    public User(String userName, String email, String passwordHash, int score) {
	this.userName = userName;
	this.email = email;
	this.passwordHash = passwordHash;
	this.score = score;
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
	return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
	this.passwordHash = passwordHash;
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
}
