package booking.payload;



import java.util.HashMap;
import java.util.Map;

public final class BookingPayload {

    private BookingPayload() {}

    public static Map<String, Object> validBooking() {

        String unique = String.valueOf(System.nanoTime());

        Map<String, Object> booking = new HashMap<>();

        booking.put("firstname", "QA" + unique);
        booking.put("lastname", "Tester");
        booking.put("totalprice", 500);
        booking.put("depositpaid", true);
        booking.put("additionalneeds", "Breakfast");

        Map<String, String> dates = new HashMap<>();
        dates.put("checkin", "2027-01-10");
        dates.put("checkout", "2027-01-15");

        booking.put("bookingdates", dates);

        return booking;
    }
    public static Map<String, Object> validBooking1() {

        String unique = String.valueOf(System.nanoTime());

        Map<String, Object> booking = new HashMap<>();

        booking.put("firstname", "QA" + unique);
        booking.put("lastname", "Tester");
        booking.put("totalprice", 500);
        booking.put("depositpaid", true);
        booking.put("additionalneeds", "Breakfast");

        Map<String, String> dates = new HashMap<>();
        dates.put("checkin", "2027-01-10");
        dates.put("checkout", "2027-01-10");

        booking.put("bookingdates", dates);

        return booking;
    }

    public static Map<String, Object> invalidPrice(int price) {
        Map<String, Object> booking = validBooking();
        booking.put("totalprice", price);
        return booking;
    }

    public static Map<String, Object> invalidDates() {
        Map<String, Object> booking = validBooking();

        Map<String, String> dates = new HashMap<>();
        dates.put("checkin", "2027-01-15");
        dates.put("checkout", "2027-01-10");

        booking.put("bookingdates", dates);
        return booking;
    }
    
    

    public static Map<String, Object> malformedDates() {
        Map<String, Object> booking = validBooking();

        Map<String, String> dates = new HashMap<>();
        dates.put("checkin", "invalid-date");
        dates.put("checkout", "2027-01-15");

        booking.put("bookingdates", dates);
        return booking;
    }

    public static Map<String, Object> emptyBooking() {
        return new HashMap<>();
    }

    public static Map<String, Object> missingFirstname() {
        Map<String, Object> booking = validBooking();
        booking.remove("firstname");
        return booking;
    }

    public static Map<String, Object> missingLastname() {
        Map<String, Object> booking = validBooking();
        booking.remove("lastname");
        return booking;
    }

    public static Map<String, Object> missingBookingDates() {
        Map<String, Object> booking = validBooking();
        booking.remove("bookingdates");
        return booking;
    }

    public static Map<String, Object> updatedBooking() {
        Map<String, Object> booking = validBooking();
        booking.put("firstname", "UpdatedQA");
        booking.put("totalprice", 800);
        return booking;
    }

    public static Map<String, Object> patchBooking() {
        Map<String, Object> booking = new HashMap<>();
        booking.put("firstname", "PatchedQA");
        return booking;
    }
}
