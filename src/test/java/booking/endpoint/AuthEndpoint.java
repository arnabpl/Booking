package booking.endpoint;


import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public final class AuthEndpoint {

    private AuthEndpoint() {}

    public static Response generateToken(
            RequestSpecification request,
            Object payload) {

        return request.body(payload).post("/auth");
    }
}