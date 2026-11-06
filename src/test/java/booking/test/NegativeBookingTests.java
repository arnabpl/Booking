package booking.test;



import booking.endpoint.BaseTest;
import booking.endpoint.BookingEndpoint;
import booking.payload.BookingPayload;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NegativeBookingTests extends BaseTest {

    private void assertInvalidBooking(Response response,
                                      String scenario) {

        Assert.assertTrue(
                response.statusCode() == 400 ||
                response.statusCode() == 422,
                scenario + " should be rejected. Actual status: "
                        + response.statusCode()
                        + ", body: " + response.asString());
    }

    private void trackIfCreated(Response response) {

        if (response.statusCode() == 200 &&
                response.jsonPath().get("bookingid") != null) {

            trackBooking(response.jsonPath().getInt("bookingid"));
        }
    }

    @Test(groups = {"negative", "critical"})
    public void NEG_001_negativeBookingPriceIsRejected() {

        Response response = BookingEndpoint.create(
                given(), BookingPayload.invalidPrice(-500));

        trackIfCreated(response);

        assertInvalidBooking(response, "Negative price");
    }

    @Test(groups = {"negative", "critical"})
    public void NEG_002_zeroBookingPriceIsRejected() {

        Response response = BookingEndpoint.create(
                given(), BookingPayload.invalidPrice(0));

        trackIfCreated(response);

        assertInvalidBooking(response, "Zero price");
    }

    @Test(groups = {"negative", "critical"})
    public void NEG_003_checkoutBeforeCheckinIsRejected() {

        Response response = BookingEndpoint.create(
                given(), BookingPayload.invalidDates());

        trackIfCreated(response);

        assertInvalidBooking(response, "Invalid date sequence");
    }

    @Test(groups = {"negative", "critical"})
    public void NEG_004_malformedBookingDatesAreRejected() {

        Response response = BookingEndpoint.create(
                given(), BookingPayload.malformedDates());

        trackIfCreated(response);

        assertInvalidBooking(response, "Malformed booking dates");
    }
}
