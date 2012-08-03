package edu.lmu.cs.headmaster.ws.service;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import edu.lmu.cs.headmaster.ws.dao.UserDao;
import edu.lmu.cs.headmaster.ws.domain.User;

/**
 * The sole implementation of the user service.
 */
@Path("/users")
public class UserServiceImpl extends AbstractService implements UserService {

    /**
     * Constructs the service.
     */
    public UserServiceImpl(UserDao userDao) {
        super(userDao);
    }

    @Override
    public List<User> getUsers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public User getUserByLogin(String login) {
        // We only allow user object access if the currently logged-in user is
        // asking about itself.
        validate(securityContext.getUserPrincipal().getName().equals(login),
                Response.Status.NOT_FOUND, USER_NOT_FOUND);

        // If we get here, either the login names match or the user was an admin.
        User user = userDao.getUserByLogin(login);
        validate(user != null, Response.Status.NOT_FOUND, USER_NOT_FOUND);
        return user;
    }

    @Override
    public Response createUser(User user) {
        validate(user.getId() == null, Response.Status.BAD_REQUEST, USER_OVERSPECIFIED);
        userDao.createUser(user);
        return Response.created(URI.create(Long.toString(user.getId()))).build();
    }

    @Override
    public Response createOrUpdateUser(Integer id, User user) {
        validate(id.equals(user.getId()), Response.Status.BAD_REQUEST, USER_INCONSISTENT);
        userDao.createOrUpdateUser(user);
        return Response.noContent().build();
    }
    
}
