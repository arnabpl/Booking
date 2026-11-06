package booking.endpoint;



import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public final class BookingEndpoint {

    private BookingEndpoint() {}

    public static Response create(
            RequestSpecification request, Object payload) {

        return request.body(payload).post("/booking");
    }

    public static Response get(
            RequestSpecification request, int id) {

        return request.get("/booking/" + id);
    }

    public static Response getAll(
            RequestSpecification request) {

        return request.get("/booking");
    }

    public static Response getFiltered(
            RequestSpecification request,
            String firstname,
            String lastname) {

        return request.queryParam("firstname", firstname)
                .queryParam("lastname", lastname)
                .get("/booking");
    }

    public static Response update(
            RequestSpecification request,
            int id,
            String token,
            Object payload) {

        return request.cookie("token", token)
                .body(payload)
                .put("/booking/" + id);
    }

    public static Response patch(
            RequestSpecification request,
            int id,
            String token,
            Object payload) {

        return request.cookie("token", token)
                .body(payload)
                .patch("/booking/" + id);
    }

    public static Response delete(
            RequestSpecification request,
            int id,
            String token) {

        return request.cookie("token", token)
                .delete("/booking/" + id);
    }
}
