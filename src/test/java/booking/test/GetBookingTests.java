package booking.test;



import booking.endpoint.BaseTest;
import booking.endpoint.BookingEndpoint;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class GetBookingTests extends BaseTest {

    @Test(groups = {"read", "smoke"})
    public void GET_001_retrieveExistingBooking() {

        int id = createBookingAndTrack();

        Response response = BookingEndpoint.get(given(), id);

        Assert.assertEquals(response.statusCode(), 200);

        Assert.assertNotNull(
                response.jsonPath().getString("firstname"));

        Assert.assertNotNull(
                response.jsonPath().get("bookingdates"));
    }

    @Test(groups = {"read", "negative"})
    public void GET_002_nonExistentBookingReturns404() {

        Response response =
                BookingEndpoint.get(given(), 999999999);

        Assert.assertEquals(response.statusCode(), 404);
    }

    @Test(groups = {"read", "functional"})
    public void GET_003_retrieveAllBookingIds() {

        Response response = BookingEndpoint.getAll(given());

        Assert.assertEquals(response.statusCode(), 200);

        List<Map<String, Object>> bookings =
                response.jsonPath().getList("$");

        Assert.assertNotNull(bookings);
    }

    @Test(groups = {"read", "functional"})
    public void GET_004_filterByFirstnameAndLastname() {

        int id = createBookingAndTrack();

        Response booking = BookingEndpoint.get(given(), id);

        String firstname = booking.jsonPath().getString("firstname");
        String lastname = booking.jsonPath().getString("lastname");

        Response response = BookingEndpoint.getFiltered(
                given(), firstname, lastname);

        Assert.assertEquals(response.statusCode(), 200);

        List<Integer> ids = response.jsonPath().getList("bookingid");

        Assert.assertNotNull(ids);
        Assert.assertTrue(
                ids.contains(id),
                "Filtered results should contain the created booking");
    }
}
