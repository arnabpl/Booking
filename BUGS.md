1. BOOK001: Negative booking price accepted

Type: Bug

High severity

Endpoint: POST /booking

Expected: HTTP 400 . Negative prices must be rejected.

Actual: HTTP 200. Booking created with totalprice: -500.

Runnable cURL:

curl -i -X POST \
  https://restful-booker.herokuapp.com/booking \
  -H "Content-Type: application/json" \
  -d '{
    "firstname": "Kumar",
    "lastname": "Pal",
    "totalprice": -4400,
    "depositpaid": true,
    "bookingdates": {
      "checkin": "2027-01-10",
      "checkout": "2027-01-15"
    },
    "additionalneeds": "Breakfast"
  }'

Severity justification: Negative booking prices can introduce invalid financial records and incorrect partner billing.
						Negative pricing can fail downstream systems, these financial data mostly consumed by legacy systems,
						and these system might crash with -ve data, or they will get a wrong totals at the end of the day
						
						
2. BOOK002: Checkout date earlier than check-in accepted

High severity

Type: Bug

Endpoint: POST /booking

Expected: HTTP 400 or 422.

Actual: HTTP 200. Booking created with an invalid date sequence.

Runnable cURL:


bash
curl -i -X POST \
  https://restful-booker.herokuapp.com/booking \
  -H "Content-Type: application/json" \
  -d '{
    "firstname": "Arnab",
    "lastname": "Pal",
    "totalprice": 700,
    "depositpaid": true,
    "bookingdates": {
      "checkin": "2027-01-15",
      "checkout": "2027-01-10"
    },
    "additionalneeds": "Breakfast"
  }'
Severity justification: Invalid reservation periods can cause booking conflicts and incorrect availability calculations.
						We may get a wrong total price and that may be -ve.
						
3. BOOK003: Abnormal booking dates accepted


High severity


Type: Bug

Endpoint: POST /booking

Expected: HTTP 400 for abnormal dates.

Actual: HTTP 200. The test with date 0NaN-aN-aN is accepted.

Runnable cURL:


bash
curl -i -X POST \
  https://restful-booker.herokuapp.com/booking \
  -H "Content-Type: application/json" \
  -d '{
    "firstname": "Arnab",
    "lastname": "Tester",
    "totalprice": 200,
    "depositpaid": true,
    "bookingdates": {
      "checkin": "invalid-date",
      "checkout": "2027-01-15"
    },
    "additionalneeds": "Breakfast"
  }'
Severity justification: Abnormal date values can corrupt reservation records and affect downstream processing.
						Even it can messup with the booking availability dates.