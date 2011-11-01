package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import edu.lmu.cs.headmaster.ws.domain.User;

/**
 * JAX-RS user service.
 */
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface UserService {

    /**
     * Returns all known users.  Only users with the headmaster role
     * should be allowed to call this URI.
     * @return the known users
     */
    @GET
    List<User> getUsers();
    
    @GET
    @Path("don")
    User getDon();
    
    @GET
    @Path("don/{identifier}")
    User getDon(@PathParam("identifier") int id);

    @GET
    @Path("toal")
    User getToal(@QueryParam("identifier") int id);
    
    @POST
    @Path("toal")
    User getToal(User mockUser);
}
