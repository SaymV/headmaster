package edu.lmu.cs.headmaster.ws.service;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

/**
 * Base class for all other service test classes to extend. It defines a shared
 * resource object for subclass convenience.
 */
public abstract class ServiceTest extends JerseyTest {

    protected WebResource ws;

    public ServiceTest() {
        super();
        ws = resource();
    }

    @Override
    protected AppDescriptor configure() {
        // The test web app descriptor nearly replicates web.xml except for the
        // Spring context and a container request filter.
        return new WebAppDescriptor.Builder("edu.lmu.cs.headmaster.ws.service")
            .contextParam("contextConfigLocation", "classpath:testContext.xml")
            .contextListenerClass(ContextLoaderListener.class)
            .requestListenerClass(RequestContextListener.class)
            .servletClass(SpringServlet.class)
            .initParam(
                "com.sun.jersey.config.property.resourceConfigClass",
                "com.sun.jersey.api.core.PackagesResourceConfig"
            )
            .initParam(
                "com.sun.jersey.config.property.packages",
                "edu.lmu.cs.headmaster.ws.service"
            )
            .initParam(
                "com.sun.jersey.spi.container.ContainerRequestFilters",
                "edu.lmu.cs.headmaster.ws.service.SecurityContextContainerRequestFilter"
            )
            .contextPath("headmaster-test").build();
    }

}
