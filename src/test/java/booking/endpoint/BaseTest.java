package booking.endpoint;



import config.Config;
import booking.payload.AuthPayload;
import booking.payload.BookingPayload;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    protected RequestSpecification requestSpec;
    private final ThreadLocal<List<Integer>> createdIds =
            ThreadLocal.withInitial(ArrayList::new);

    @BeforeClass(alwaysRun = true)
    public void setupRequestSpecification() {

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(Config.getBaseUrl())
                .setContentType("application/json")
                .setAccept("application/json")
                .setConfig(RestAssuredConfig.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam(
                                        "http.connection.timeout",
                                        Config.getConnectionTimeout())
                                .setParam(
                                        "http.socket.timeout",
                                        Config.getSocketTimeout())))
                .build();
    }

    protected RequestSpecification given() {
        return RestAssured.given().spec(requestSpec);
    }

    @BeforeMethod(alwaysRun = true)
    public void initializeTestData() {
        createdIds.set(new ArrayList<>());
    }

    protected String getToken() {

        Response response = AuthEndpoint.generateToken(
                given(), AuthPayload.validCredentials());

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("token"));

        return response.jsonPath().getString("token");
    }

    protected int createBookingAndTrack() {

        Response response = BookingEndpoint.create(
                given(), BookingPayload.validBooking());

        Assert.assertEquals(
                response.statusCode(), 200,
                "Booking creation failed: " + response.asString());

        int id = response.jsonPath().getInt("bookingid");
        createdIds.get().add(id);

        return id;
    }

    protected void trackBooking(int id) {
        createdIds.get().add(id);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupBookings() {

        try {
            String token = getToken();

            for (Integer id : createdIds.get()) {
                try {
                    BookingEndpoint.delete(given(), id, token);
                } catch (Exception e) {
                    System.err.println(
                            "Cleanup failed for booking ID " + id);
                }
            }

        } finally {
            createdIds.remove();
        }
    }
}
