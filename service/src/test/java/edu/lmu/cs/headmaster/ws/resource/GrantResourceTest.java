package edu.lmu.cs.headmaster.ws.resource;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import edu.lmu.cs.headmaster.ws.domain.Grant;
import edu.lmu.cs.headmaster.ws.util.DomainObjectUtils;

/**
 * Tests the grant web resource.
 */
public class GrantResourceTest extends ResourceTest {

    @Test
    public void testGetGrantByNonexistentId() {
        ClientResponse clientResponse = wr.path("grants/17").get(ClientResponse.class);
        Assert.assertEquals(404, clientResponse.getStatus());
    }

    @Test
    public void testGetGrantById() {
        Grant grant = wr.path("grants/1000000").get(ClientResponse.class).getEntity(Grant.class);
        Assert.assertEquals(Long.valueOf(1000000L), grant.getId());
        Assert.assertEquals(Integer.valueOf(10000), grant.getAmount());
        Assert.assertEquals("Leonard Kleinrock", grant.getFacultyMentor());
        Assert.assertEquals("The Worldwide Web", grant.getTitle());
    }

    @Test
    public void testGetGrantsQueryByName() {
        List<Grant> grants = wr.path("grants").queryParam("q", "leonard")
                .get(new GenericType<List<Grant>>() { });

        Assert.assertEquals(1, grants.size());
    }

    @Test
    public void testGetGrantsQueryByTitle() {
        List<Grant> grants = wr.path("grants").queryParam("q", "worldwide")
                .get(new GenericType<List<Grant>>() { });

        Assert.assertEquals(1, grants.size());
    }

    @Test(expected=com.sun.jersey.api.client.UniformInterfaceException.class)
    public void testGetGrantsByNullQuery() {
        List<Grant> grants = wr.path("grants")
                .get(new GenericType<List<Grant>>() { });

        Assert.assertEquals(1, grants.size());
    }

    @Test
    public void testGetGrantsByBooleanQuery() {
        List<Grant> grants = wr.path("grants").queryParam("presented", "false")
                .get(new GenericType<List<Grant>>() { });

        Assert.assertEquals(2, grants.size());
    }

    @Test
    public void testGetGrantsByAwardedQuery() {
        List<Grant> grants = wr.path("grants").queryParam("awarded", "AWARDED")
                .get(new GenericType<List<Grant>>() { });

        Assert.assertEquals(1, grants.size());
    }

    @Test
    public void testCreateGrant() {
        Grant grantToCreate = DomainObjectUtils.createGrantObject(1024, "Tyler Nichols", "Marc Papkyriakou");

        ClientResponse response = wr.path("grants").post(ClientResponse.class, grantToCreate);
        Assert.assertEquals(201, response.getStatus());
    }

    @Test
    public void testCreateGrantWithSpecifiedIdProduces400() {
        Grant grantToCreate = DomainObjectUtils.createGrantObject(1024, "Tyler Nichols", "Marc Papkyriakou lol");

        grantToCreate.setId(500L);

        ClientResponse response = wr.path("grants").post(ClientResponse.class, grantToCreate);
        Assert.assertEquals(400, response.getStatus());
    }

    @Test
    public void testUpdateGrantProduces204() {
        Grant grantToUpdate = DomainObjectUtils.createGrantObject(1000, "Andy Won", "Won Grant");

        grantToUpdate.setId(1000000L);

        ClientResponse response = wr.path("grants/1000000").put(ClientResponse.class, grantToUpdate);
        Assert.assertEquals(204, response.getStatus());
    }

    @Test
    public void testUpdateGrantWithInconsistentIdProduces400() {
        Grant grantToUpdate = DomainObjectUtils.createGrantObject(1000, "Andy Won", "Won Grant");

        ClientResponse response = wr.path("grants/1000").put(ClientResponse.class, grantToUpdate);
        Assert.assertEquals(400, response.getStatus());
    }

}
