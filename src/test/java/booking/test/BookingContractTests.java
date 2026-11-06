package booking.test;



import booking.endpoint.BaseTest;
import booking.endpoint.BookingEndpoint;
import booking.payload.BookingPayload;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class BookingContractTests extends BaseTest {

    private void validateBookingFields(Map<String, Object> booking) {

        Assert.assertNotNull(booking.get("firstname"));
        Assert.assertTrue(booking.get("firstname") instanceof String);

        Assert.assertNotNull(booking.get("lastname"));
        Assert.assertTrue(booking.get("lastname") instanceof String);

        Assert.assertNotNull(booking.get("totalprice"));
        Assert.assertTrue(booking.get("totalprice") instanceof Number);

        Assert.assertNotNull(booking.get("depositpaid"));
        Assert.assertTrue(booking.get("depositpaid") instanceof Boolean);

        Assert.assertNotNull(booking.get("bookingdates"));
        Assert.assertTrue(booking.get("bookingdates") instanceof Map);

        Map<?, ?> dates = (Map<?, ?>) booking.get("bookingdates");

        Assert.assertTrue(dates.get("checkin") instanceof String);
        Assert.assertTrue(dates.get("checkout") instanceof String);
    }

    @Test(groups = {"contract"})
    public void CONTRACT_001_validateCreateBookingResponse() {

        Response response = BookingEndpoint.create(
                given(), BookingPayload.validBooking());

        Assert.assertEquals(response.statusCode(), 200);

        int id = response.jsonPath().getInt("bookingid");
        trackBooking(id);

        Assert.assertTrue(id > 0);

        Map<String, Object> booking =
                response.jsonPath().getMap("booking");

        Assert.assertNotNull(booking);

        validateBookingFields(booking);
    }

    @Test(groups = {"contract"})
    public void CONTRACT_002_validateGetBookingResponse() {

        int id = createBookingAndTrack();

        Response response = BookingEndpoint.get(given(), id);

        Assert.assertEquals(response.statusCode(), 200);

        Map<String, Object> booking = response.jsonPath().getMap("$");

        Assert.assertNotNull(booking);

        validateBookingFields(booking);
    }
}
