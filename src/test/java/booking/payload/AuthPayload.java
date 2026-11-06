package booking.payload;


import config.Config;

import java.util.HashMap;
import java.util.Map;

public final class AuthPayload {

    private AuthPayload() {}

    public static Map<String, String> validCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", Config.getUsername());
        credentials.put("password", Config.getPassword());
        return credentials;
    }

    public static Map<String, String> invalidCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "wronguser");
        credentials.put("password", "wrongpassword");
        return credentials;
    }
}