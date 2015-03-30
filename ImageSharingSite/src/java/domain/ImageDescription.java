package domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Frederik
 */
@Entity
public class ImageDescription implements Serializable {

    @Id
    @Column(length = 5)
    private String id;

    private String title;
    private String description;
    private String fileReference;
    
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne
    private User owner;

    public ImageDescription() {
    }

    public ImageDescription(String id, String title, String description, String fileReference, Visibility visibility, User owner) {
	this.id = id;
	this.title = title;
	this.description = description;
	this.fileReference = fileReference;
	this.visibility = visibility;
	this.owner = owner;
    }
    
    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getFileReference() {
	return fileReference;
    }

    public void setFileReference(String fileReference) {
	this.fileReference = fileReference;
    }

    public Visibility getVisibility() {
	return visibility;
    }

    public void setVisibility(Visibility visibility) {
	this.visibility = visibility;
    }

    public User getOwner() {
	return owner;
    }

    public void setOwner(User owner) {
	this.owner = owner;
    }
}
