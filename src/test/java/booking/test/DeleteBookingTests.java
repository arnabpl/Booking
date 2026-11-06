package booking.test;



import booking.endpoint.BaseTest;
import booking.endpoint.BookingEndpoint;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteBookingTests extends BaseTest {

    @Test(groups = {"delete", "smoke"})
    public void DELETE_001_deleteExistingBooking() {

        int id = createBookingAndTrack();
        String token = getToken();

        Response response = BookingEndpoint.delete(
                given(), id, token);

        Assert.assertEquals(response.statusCode(), 201);

        // Remove from cleanup because it has already been deleted.
        Response getResponse = BookingEndpoint.get(given(), id);

        Assert.assertEquals(getResponse.statusCode(), 404);
    }

    @Test(groups = {"delete", "functional"})
    public void DELETE_002_deletedBookingCannotBeRetrieved() {

        int id = createBookingAndTrack();
        String token = getToken();

        Response deleteResponse = BookingEndpoint.delete(
                given(), id, token);

        Assert.assertEquals(deleteResponse.statusCode(), 201);

        Response getResponse = BookingEndpoint.get(given(), id);

        Assert.assertEquals(getResponse.statusCode(), 404);
    }
}
