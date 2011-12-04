package edu.lmu.cs.headmaster.ws.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * A simplified exception class useful in JAX-RS applications.  Allows responses to be
 * build with an HTTP response code, the content type text/plain and a single message.
 */
public class ServiceException extends WebApplicationException {

    public ServiceException(int status, String message) {
        super(Response.status(status)
                .header("Content-type", "text/plain")
                .entity(status + " " + message)
                .build());
    }

}
