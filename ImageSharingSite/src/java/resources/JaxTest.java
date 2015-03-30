package resources;

import domain.ImageDescription;
import domain.User;
import domain.Visibility;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Frederik
 */
@RequestScoped
@Transactional
@Path("init")
public class JaxTest {

    @PersistenceContext
    private EntityManager manager;

    @GET
    public String initializeDatabase() {
//	try {
//	    User frederik = new User("FrederikDS", "frederik.de.smedt@hotmail.com", "", 9001);
//	    User brent = new User("BrentC", "brent.couck@gmail.com", "", 5);
//	    User pj = new User("PieterJanG", "pieterjangeeroms@hotmail.com", "", 10);
//
//	    ImageDescription descriptionImage1 = new ImageDescription("12345", "Image 1", "The first image", "", Visibility.PUBLIC, frederik);
//	    ImageDescription descriptionImage2 = new ImageDescription("123ab", "Image 2", "The second image", "", Visibility.UNLISTED, brent);
//	    ImageDescription descriptionImage3 = new ImageDescription("abcde", "Image 3", "The third image", "", Visibility.PRIVATE, pj);
//	    ImageDescription descriptionImage4 = new ImageDescription("popo1", "Image 4", "The fourth image", "", Visibility.PUBLIC, pj);
//
//	    manager.persist(frederik);
//	    manager.persist(brent);
//	    manager.persist(pj);
//	    manager.persist(descriptionImage1);
//	    manager.persist(descriptionImage2);
//	    manager.persist(descriptionImage3);
//	    manager.persist(descriptionImage4);
//	} catch (Exception e) {
//	    return e.getMessage();
//	}

	return "ok";
    }
}
