package edu.lmu.cs.headmaster.ws.dao;

import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.util.ApplicationContextTest;
import edu.lmu.cs.headmaster.ws.util.DomainObjectUtils;

public class EventDaoTest extends ApplicationContextTest {

    private EventDao eventDao = (EventDao)applicationContext.getBean("eventDao");
    private StudentDao studentDao = (StudentDao)applicationContext.getBean("studentDao");

    @Test
    public void testGetEventById() {
        // Grab the known event in the fixture.
        Event event = eventDao.getEventById(1000000L);
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
    public void testCreateEventWithoutStudents() {
        // Create an id-less event.
        Event eventToCreate = DomainObjectUtils.createEventObject(
            "Reading", "Rainbow", new DateTime(2013, 3, 12, 6, 6, 7, 0)
        );

        // Save the event.
        eventDao.createEvent(eventToCreate);

        // The created event should now have an ID. Because we know what is in
        // the test fixture, we know what ID to expect.
        Assert.assertEquals(Long.valueOf(1L), eventToCreate.getId());

        // Reload the event that was just created.
        Event createdEvent = eventDao.getEventById(1L);
        assertSimpleEquality(eventToCreate, createdEvent);
        Assert.assertEquals(0, createdEvent.getAttendees().size());
    }

    @Test
    public void testCreateEventWithStudents() {
        // Create an id-less event.
        Event eventToCreate = DomainObjectUtils.createEventObject(
            "Big meeting", "Very important", new DateTime(2013, 12, 24, 11, 59, 59, 0)
        );

        // Have two students attend it.
        eventToCreate.getAttendees().add(studentDao.getStudentById(1000001L));
        eventToCreate.getAttendees().add(studentDao.getStudentById(1000002L));

        // Save the event.
        eventDao.createEvent(eventToCreate);

        // The created event should now have an ID. Because we know what is in
        // the test fixture, we know what ID to expect.
        Assert.assertEquals(Long.valueOf(1L), eventToCreate.getId());

        // Reload the event that was just created. Because we know what is in the
        // test fixture, we know what ID to expect.
        Event createdEvent = eventDao.getEventById(1L);
        assertSimpleEquality(eventToCreate, createdEvent);

        // Check the list of attendees (by ID only in this test).
        Assert.assertEquals(2, createdEvent.getAttendees().size());
        List<Student> attendees = createdEvent.getAttendees();
        Assert.assertEquals(Long.valueOf(1000001L), attendees.get(0).getId());
        Assert.assertEquals(Long.valueOf(1000002L), attendees.get(1).getId());
    }

    /**
     * Helper function for asserting the equality of two events.
     */
    private void assertSimpleEquality(Event event1, Event event2) {
        Assert.assertEquals(event1.getId(), event2.getId());
        Assert.assertEquals(event1.getTitle(), event2.getTitle());
        Assert.assertEquals(event1.getDescription(), event2.getDescription());
        Assert.assertEquals(event1.getDateTime(), event2.getDateTime());
    }
}
