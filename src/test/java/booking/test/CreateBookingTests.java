package booking.test;



import booking.endpoint.BaseTest;
import booking.endpoint.BookingEndpoint;
import booking.payload.BookingPayload;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class CreateBookingTests extends BaseTest {

    @Test(groups = {"create", "smoke"})
    public void CREATE_001_createBookingSuccessfully() {

        Response response = BookingEndpoint.create(
                given(), BookingPayload.validBooking());

        Assert.assertEquals(response.statusCode(), 200);

        int id = response.jsonPath().getInt("bookingid");
        trackBooking(id);

        Assert.assertTrue(id > 0);
        Assert.assertNotNull(response.jsonPath().get("booking"));
    }
    
    @Test(groups = {"create", "functional"})
    public void CREATE_002_createBookingSuccessfully() {

        Response response = BookingEndpoint.create(
                given(), BookingPayload.validBooking());

        Assert.assertEquals(response.statusCode(), 200);

        int id = response.jsonPath().getInt("bookingid");
        trackBooking(id);

        Assert.assertTrue(id > 0);
        Assert.assertNotNull(response.jsonPath().get("booking"));
    }

    @Test(groups = {"create", "functional"})
    public void CREATE_003_validateAllBookingFields() {

        Map<String, Object> payload = BookingPayload.validBooking();

        Response response = BookingEndpoint.create(given(), payload);

        Assert.assertEquals(response.statusCode(), 200);

        int id = response.jsonPath().getInt("bookingid");
        trackBooking(id);

        Assert.assertEquals(
                response.jsonPath().getString("booking.firstname"),
                payload.get("firstname"));

        Assert.assertEquals(
                response.jsonPath().getString("booking.lastname"),
                payload.get("lastname"));

        Assert.assertEquals(
                response.jsonPath().getInt("booking.totalprice"),
                payload.get("totalprice"));

        Assert.assertEquals(
                response.jsonPath().getBoolean("booking.depositpaid"),
                payload.get("depositpaid"));
    }

    @Test(groups = {"create", "negative"})
    public void CREATE_004_missingFirstnameIsRejected() {

        Response response = BookingEndpoint.create(
                given(), BookingPayload.missingFirstname());

        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test(groups = {"create", "negative"})
    public void CREATE_005_missingLastnameIsRejected() {

        Response response = BookingEndpoint.create(
                given(), BookingPayload.missingLastname());

        Assert.assertEquals(response.statusCode(), 400);
    }

    @Test(groups = {"create", "negative"})
    public void CREATE_006_missingBookingDatesIsRejected() {

        Response response = BookingEndpoint.create(
                given(), BookingPayload.missingBookingDates());

        Assert.assertEquals(response.statusCode(), 400);
    }
}
