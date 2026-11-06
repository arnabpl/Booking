package booking.test;



import booking.endpoint.BaseTest;
import booking.endpoint.BookingEndpoint;
import booking.endpoint.AuthEndpoint;
import booking.payload.BookingPayload;
import booking.payload.AuthPayload;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class BookingE2E extends BaseTest {

    
//     TEST 1:
//     Complete API chaining:
//     Create -> Read -> PUT -> Read -> PATCH -> Read -> Delete -> Verify
     

    @Test(groups = {"smoke", "e2e", "critical"})
    public void E2E_001_completeBookingCRUDJourney() {

        // STEP 1: CREATE BOOKING

        Map<String, Object> createPayload =
                BookingPayload.validBooking();

        Response createResponse = BookingEndpoint.create(
                given(), createPayload);

        Assert.assertEquals(
                createResponse.statusCode(), 200,
                "Booking creation should return HTTP 200");

        Assert.assertNotNull(
                createResponse.jsonPath().get("bookingid"),
                "Booking ID should be generated");

        Assert.assertNotNull(
                createResponse.jsonPath().get("booking"),
                "Booking object should be returned");

        int bookingId =
                createResponse.jsonPath().getInt("bookingid");

        trackBooking(bookingId);

        Assert.assertTrue(bookingId > 0);

        Assert.assertEquals(
                createResponse.jsonPath().getString(
                        "booking.firstname"),
                createPayload.get("firstname"));

        Assert.assertEquals(
                createResponse.jsonPath().getString(
                        "booking.lastname"),
                createPayload.get("lastname"));

        Assert.assertEquals(
                createResponse.jsonPath().getInt(
                        "booking.totalprice"),
                createPayload.get("totalprice"));

        System.out.println(
                "STEP 1 PASSED: Booking created. ID = " + bookingId);


        // STEP 2: READ CREATED BOOKING

        Response getResponse =
                BookingEndpoint.get(given(), bookingId);

        Assert.assertEquals(
                getResponse.statusCode(), 200,
                "GET should return HTTP 200");

        Assert.assertEquals(
                getResponse.jsonPath().getString("firstname"),
                createPayload.get("firstname"));

        Assert.assertEquals(
                getResponse.jsonPath().getString("lastname"),
                createPayload.get("lastname"));

        Assert.assertEquals(
                getResponse.jsonPath().getInt("totalprice"),
                createPayload.get("totalprice"));

        System.out.println(
                "STEP 2 PASSED: Booking retrieved successfully");


        // STEP 3: GENERATE AUTH TOKEN

        Response authResponse = AuthEndpoint.generateToken(
                given(), AuthPayload.validCredentials());

        Assert.assertEquals(
                authResponse.statusCode(), 200,
                "Authentication should return HTTP 200");

        String token =
                authResponse.jsonPath().getString("token");

        Assert.assertNotNull(
                token, "Authentication token should not be null");

        Assert.assertFalse(
                token.isEmpty(), "Authentication token should not be empty");

        System.out.println(
                "STEP 3 PASSED: Authentication token generated");


        // STEP 4: FULL UPDATE USING PUT

        Map<String, Object> putPayload =
                BookingPayload.updatedBooking();

        Response putResponse = BookingEndpoint.update(
                given(), bookingId, token, putPayload);

        Assert.assertEquals(
                putResponse.statusCode(), 200,
                "PUT should return HTTP 200");

        Assert.assertEquals(
                putResponse.jsonPath().getString("firstname"),
                putPayload.get("firstname"));

        Assert.assertEquals(
                putResponse.jsonPath().getString("lastname"),
                putPayload.get("lastname"));

        Assert.assertEquals(
                putResponse.jsonPath().getInt("totalprice"),
                putPayload.get("totalprice"));

        System.out.println(
                "STEP 4 PASSED: Booking updated using PUT");


        // STEP 5: VERIFY PUT UPDATE

        Response afterPutResponse =
                BookingEndpoint.get(given(), bookingId);

        Assert.assertEquals(afterPutResponse.statusCode(), 200);

        Assert.assertEquals(
                afterPutResponse.jsonPath().getString("firstname"),
                putPayload.get("firstname"));

        Assert.assertEquals(
                afterPutResponse.jsonPath().getInt("totalprice"),
                putPayload.get("totalprice"));

        System.out.println(
                "STEP 5 PASSED: PUT changes persisted");


        // STEP 6: PARTIAL UPDATE USING PATCH

        Map<String, Object> patchPayload =
                BookingPayload.patchBooking();

        Response patchResponse = BookingEndpoint.patch(
                given(), bookingId, token, patchPayload);

        Assert.assertEquals(
                patchResponse.statusCode(), 200,
                "PATCH should return HTTP 200");

        Assert.assertEquals(
                patchResponse.jsonPath().getString("firstname"),
                patchPayload.get("firstname"));

        System.out.println(
                "STEP 6 PASSED: Booking updated using PATCH");


        // STEP 7: VERIFY PATCH AND PRESERVED FIELDS

        Response afterPatchResponse =
                BookingEndpoint.get(given(), bookingId);

        Assert.assertEquals(afterPatchResponse.statusCode(), 200);

        Assert.assertEquals(
                afterPatchResponse.jsonPath().getString("firstname"),
                patchPayload.get("firstname"));

        Assert.assertEquals(
                afterPatchResponse.jsonPath().getString("lastname"),
                putPayload.get("lastname"),
                "PATCH should preserve unchanged lastname");

        Assert.assertEquals(
                afterPatchResponse.jsonPath().getInt("totalprice"),
                putPayload.get("totalprice"),
                "PATCH should preserve unchanged totalprice");

        System.out.println(
                "STEP 7 PASSED: PATCH changes persisted");


        // STEP 8: DELETE BOOKING

        Response deleteResponse = BookingEndpoint.delete(
                given(), bookingId, token);

        Assert.assertEquals(
                deleteResponse.statusCode(), 201,
                "DELETE should return HTTP 201");

        System.out.println(
                "STEP 8 PASSED: Booking deleted");


        // STEP 9: VERIFY BOOKING DELETION

        Response deletedGetResponse =
                BookingEndpoint.get(given(), bookingId);

        Assert.assertEquals(
                deletedGetResponse.statusCode(), 404,
                "Deleted booking should no longer be available");

        System.out.println(
                "STEP 9 PASSED: Deleted booking returns HTTP 404");
    }


    /*
     * TEST 2:
     * Validate successful token retrieval.
     */

    @Test(groups = {"auth", "smoke"})
    public void E2E_002_validateTokenGeneration() {

        Response response = AuthEndpoint.generateToken(
                given(), AuthPayload.validCredentials());

        Assert.assertEquals(response.statusCode(), 200);

        String token = response.jsonPath().getString("token");

        Assert.assertNotNull(token);
        Assert.assertFalse(token.isEmpty());

        Assert.assertEquals(
                response.jsonPath().getMap("$").size(),
                1,
                "Successful authentication should return token object");

        System.out.println(
                "Token generation successful");
    }


    /*
     * TEST 3:
     * Validate that PUT without authentication is rejected.
     */

    @Test(groups = {"auth", "security"})
    public void E2E_003_putWithoutAuthenticationShouldBeRejected() {

        int bookingId = createBookingAndTrack();

        Response response = given()
                .body(BookingPayload.updatedBooking())
                .put("/booking/" + bookingId);

        Assert.assertEquals(
                response.statusCode(), 403,
                "PUT without authentication should return HTTP 403");

        // Verify original booking remains unchanged.

        Response getResponse =
                BookingEndpoint.get(given(), bookingId);

        Assert.assertEquals(getResponse.statusCode(), 200);

        Assert.assertEquals(
                getResponse.jsonPath().getString("firstname"),
                "QA" + ""); // Replace with original payload assertion
    }


    /*
     * TEST 4:
     * Validate that PATCH without authentication is rejected.
     */

    @Test(groups = {"auth", "security"})
    public void E2E_004_patchWithoutAuthenticationShouldBeRejected() {

        int bookingId = createBookingAndTrack();

        Response response = given()
                .body(BookingPayload.patchBooking())
                .patch("/booking/" + bookingId);

        Assert.assertEquals(
                response.statusCode(), 403,
                "PATCH without authentication should return HTTP 403");
    }


    /*
     * TEST 5:
     * Validate that DELETE without authentication is rejected.
     */

    @Test(groups = {"auth", "security"})
    public void E2E_005_deleteWithoutAuthenticationShouldBeRejected() {

        int bookingId = createBookingAndTrack();

        Response response = given()
                .delete("/booking/" + bookingId);

        Assert.assertEquals(
                response.statusCode(), 403,
                "DELETE without authentication should return HTTP 403");

        // Booking must still exist.

        Response getResponse =
                BookingEndpoint.get(given(), bookingId);

        Assert.assertEquals(
                getResponse.statusCode(), 200,
                "Unauthenticated DELETE must not remove the booking");
    }


    /*
     * TEST 6:
     * Validate that PUT with invalid authentication is rejected.
     */

    @Test(groups = {"auth", "security"})
    public void E2E_006_putWithInvalidTokenShouldBeRejected() {

        int bookingId = createBookingAndTrack();

        Response response = given()
                .cookie("token", "invalid-token")
                .body(BookingPayload.updatedBooking())
                .put("/booking/" + bookingId);

        Assert.assertEquals(
                response.statusCode(), 403,
                "PUT with invalid token should return HTTP 403");
    }


    /*
     * TEST 7:
     * Validate that PATCH with invalid authentication is rejected.
     */

    @Test(groups = {"auth", "security"})
    public void E2E_007_patchWithInvalidTokenShouldBeRejected() {

        int bookingId = createBookingAndTrack();

        Response response = given()
                .cookie("token", "invalid-token")
                .body(BookingPayload.patchBooking())
                .patch("/booking/" + bookingId);

        Assert.assertEquals(
                response.statusCode(), 403,
                "PATCH with invalid token should return HTTP 403");
    }


    /*
     * TEST 8:
     * Validate that DELETE with invalid authentication is rejected.
     */

    @Test(groups = {"auth", "security"})
    public void E2E_008_deleteWithInvalidTokenShouldBeRejected() {

        int bookingId = createBookingAndTrack();

        Response response = given()
                .cookie("token", "invalid-token")
                .delete("/booking/" + bookingId);

        Assert.assertEquals(
                response.statusCode(), 403,
                "DELETE with invalid token should return HTTP 403");

        // Verify booking still exists.

        Response getResponse =
                BookingEndpoint.get(given(), bookingId);

        Assert.assertEquals(
                getResponse.statusCode(), 200,
                "Invalid token must not delete the booking");
    }
}
