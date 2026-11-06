package booking.endpoint;


import config.Config;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

public final class HealthCheck {

    private HealthCheck() {}

    public static Response checkHealth() {

        return RestAssured.given()
                .baseUri(Config.getBaseUrl())
                .config(RestAssuredConfig.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam(
                                        "http.connection.timeout",
                                        Config.getConnectionTimeout())
                                .setParam(
                                        "http.socket.timeout",
                                        Config.getSocketTimeout())))
                .get("/ping");
    }
}