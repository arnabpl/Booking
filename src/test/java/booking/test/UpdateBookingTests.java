package booking.test;



import booking.endpoint.BaseTest;
import booking.endpoint.BookingEndpoint;
import booking.payload.BookingPayload;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class UpdateBookingTests extends BaseTest {

    @Test(groups = {"update", "smoke"})
    public void UPDATE_001_fullUpdateUsingPUT() {

        int id = createBookingAndTrack();
        String token = getToken();

        Map<String, Object> payload =
                BookingPayload.updatedBooking();

        Response response = BookingEndpoint.update(
                given(), id, token, payload);

        Assert.assertEquals(response.statusCode(), 200);

        Assert.assertEquals(
                response.jsonPath().getString("firstname"),
                "UpdatedQA");

        Assert.assertEquals(
                response.jsonPath().getInt("totalprice"),
                800);
    }

    @Test(groups = {"update", "smoke"})
    public void UPDATE_002_partialUpdateUsingPATCH() {

        int id = createBookingAndTrack();
        String token = getToken();

        Response response = BookingEndpoint.patch(
                given(), id, token, BookingPayload.patchBooking());

        Assert.assertEquals(response.statusCode(), 200);

        Assert.assertEquals(
                response.jsonPath().getString("firstname"),
                "PatchedQA");

        // Verify that PATCH preserved unchanged fields.
        Response getResponse = BookingEndpoint.get(given(), id);

        Assert.assertEquals(getResponse.statusCode(), 200);
        Assert.assertEquals(
                getResponse.jsonPath().getString("lastname"),
                "Tester");
        Assert.assertEquals(
                getResponse.jsonPath().getInt("totalprice"),
                500);
    }

    @Test(groups = {"update", "security"})
    public void UPDATE_003_putWithoutAuthenticationIsRejected() {

        int id = createBookingAndTrack();

        Response response = given()
                .body(BookingPayload.updatedBooking())
                .put("/booking/" + id);

        Assert.assertEquals(response.statusCode(), 403);
    }

    @Test(groups = {"update", "security"})
    public void UPDATE_004_patchWithInvalidTokenIsRejected() {

        int id = createBookingAndTrack();

        Response response = given()
                .cookie("token", "invalid-token")
                .body(BookingPayload.patchBooking())
                .patch("/booking/" + id);

        Assert.assertEquals(response.statusCode(), 403);
    }
}
