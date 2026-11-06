package booking.test;



import booking.endpoint.AuthEndpoint;
import booking.endpoint.BaseTest;
import booking.endpoint.BookingEndpoint;
import booking.payload.AuthPayload;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTests extends BaseTest {

    @Test(groups = {"auth", "smoke"})
    public void AUTH_001_validCredentialsGenerateToken() {

        Response response = AuthEndpoint.generateToken(
                given(), AuthPayload.validCredentials());

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("token"));
    }

    @Test(groups = {"auth", "negative"})
    public void AUTH_002_invalidCredentialsAreRejected() {

        Response response = AuthEndpoint.generateToken(
                given(), AuthPayload.invalidCredentials());

        Assert.assertEquals(response.statusCode(), 200);

        Assert.assertNull(
                response.jsonPath().get("token"),
                "Invalid credentials must not generate a token");

        Assert.assertEquals(
                response.jsonPath().getString("reason"),
                "Bad credentials");
    }

    @Test(groups = {"auth", "security"})
    public void AUTH_003_putWithoutAuthenticationIsRejected() {

        int id = createBookingAndTrack();

        Response response = BookingEndpoint.update(
                given(), id, null,
                booking.payload.BookingPayload.updatedBooking());

        Assert.assertEquals(response.statusCode(), 403);
    }

    @Test(groups = {"auth", "security"})
    public void AUTH_004_deleteWithInvalidTokenIsRejected() {

        int id = createBookingAndTrack();

        Response response = given()
                .cookie("token", "invalid-token")
                .delete("/booking/" + id);

        Assert.assertEquals(response.statusCode(), 403);
    }
}
