package resources;

import domain.User;
import domain.validation.groups.OnUserAdd;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author Frederik
 */
@Path("users")
@Transactional(dontRollbackOn = {BadRequestException.class})
@RequestScoped
public class Users {

    @PersistenceContext
    private EntityManager manager;

    @Context
    private SecurityContext context;

    @Resource
    private Validator validator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{username}")
    public Response getPublicUserData(@PathParam("username") String username) {
	User user = manager.find(User.class, username);
	if (user == null) {
	    return Response.noContent().build();
	}

	user.setPrivilege(username != null && context.getUserPrincipal() != null && username.equals(context.getUserPrincipal().getName()) ? User.UserPrivilege.RESTRICTED : User.UserPrivilege.PUBLIC);
	return Response.ok(user).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addUser(User user) {
	Set<ConstraintViolation<User>> violations = validator.validate(user, OnUserAdd.class);

	if (!violations.isEmpty()) {
	    throw new BadRequestException(violations.stream().map(ConstraintViolation::getMessage).reduce("", (s1, s2) -> s1 + "\n" + s2));
	}

	if (manager.find(User.class, user.getUserName()) != null) {
	    throw new BadRequestException("Username is already in use.");
	}

	user.setPasswordHash(User.createPasswordHash(user.getPassword()));
	manager.persist(user);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
	return manager.createNamedQuery("User.findAll", User.class).getResultList();
    }

    @GET
    @Path("login")
    public String login(@HeaderParam("username") String username, @HeaderParam("password") String password) {
	return "Basic " + new String(Base64.getEncoder().encode((username + ":" + User.createPasswordHash(password)).getBytes()));
    }
}
