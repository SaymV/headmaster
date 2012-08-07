package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.util.DomainObjectUtils;

/**
 * Tests the event web service.
 */
public class EventServiceTest extends ServiceTest {

    @Test
    public void testGetEventById() {
        Event event = ws.path("events/1000000").get(ClientResponse.class).getEntity(Event.class);
        Assert.assertEquals(Long.valueOf(1000000L), event.getId());
        Assert.assertEquals("Summit", event.getTitle());
        Assert.assertEquals("The big one", event.getDescription());
        Assert.assertEquals(new DateTime(2012, 7, 28, 10, 31, 3, 0), event.getDateTime());
        Assert.assertEquals(3, event.getAttendees().size());

        // ID tests will suffice here. We leave another unit test to validate
        // the loading of entire student objects.
        List<Student> attendees = event.getAttendees();
        Assert.assertEquals(Long.valueOf(1000002L), attendees.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000000L), attendees.get(1).getId());
        Assert.assertEquals(Long.valueOf(1000001L), attendees.get(2).getId());
    }

    @Test
    public void testCreateEvent() {
        // Create an id-less event.
        Event eventToCreate = DomainObjectUtils.createEventObject(
            "Reading", "Rainbow", new DateTime(2013, 3, 12, 6, 6, 7, 0)
        );

        // Now, save it.  We should get a 201 with a location.
        ClientResponse response = ws.path("events").post(ClientResponse.class, eventToCreate);
        Assert.assertEquals(201, response.getStatus());

        // Per our database fixture, we know the new user ID (and therefore location) to expect.
        Assert.assertEquals(1, response.getHeaders().get("Location").size());
        Assert.assertEquals(ws.getURI() + "/events/1", response.getHeaders().getFirst("Location"));
    }

    @Test
    public void testCreateOverspecifiedEvent() {
        // Create an event with an ID.
        Event eventToCreate = DomainObjectUtils.createEventObject(
            "Reading", "Rainbow", new DateTime(2013, 3, 12, 6, 6, 7, 0)
        );
        eventToCreate.setId(80789L);

        // This time, we should not be able to save the event: status 400.
        ClientResponse response = ws.path("events").post(ClientResponse.class, eventToCreate);

        // We expect error 400, EVENT_OVERSPECIFIED.
        Assert.assertEquals(400, response.getStatus());
        Assert.assertEquals(
            "400 " + EventService.EVENT_OVERSPECIFIED,
            response.getEntity(String.class)
        );
    }

}
