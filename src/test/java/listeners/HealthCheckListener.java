package listeners;



import config.Config;
import booking.endpoint.HealthCheck;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;

import java.util.concurrent.atomic.AtomicBoolean;

public class HealthCheckListener implements IInvokedMethodListener {

    private static final AtomicBoolean CHECK_COMPLETED =
            new AtomicBoolean(false);

    private static volatile boolean apiHealthy = false;

    @Override
    public void beforeInvocation(
            IInvokedMethod method,
            ITestResult result) {

        if (!method.isTestMethod()) {
            return;
        }

        if (CHECK_COMPLETED.compareAndSet(false, true)) {
            performHealthCheck();
        }

        if (!apiHealthy) {
            throw new SkipException(
                    "API unavailable. Test skipped because "
                            + "the suite health check failed.");
        }
    }

    private void performHealthCheck() {

        int attempts = Config.getHealthMaxAttempts();

        for (int i = 1; i <= attempts; i++) {

            try {
                System.out.println(
                        "Health check attempt: " + i);

                int status = HealthCheck.checkHealth().statusCode();

                if (status == 201) {
                    apiHealthy = true;

                    ExtentReportListener.log(
                            "API Health Check: PASSED (HTTP 201)");

                    System.out.println("API is healthy.");
                    return;
                }

                System.out.println(
                        "Unexpected health status: " + status);

            } catch (Exception e) {

                System.out.println(
                        "Health check error: " + e.getMessage());
            }

            if (i < attempts) {
                try {
                    Thread.sleep(Config.getHealthRetryDelay());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        ExtentReportListener.log(
                "API Health Check: FAILED. Test suite blocked.");

        apiHealthy = false;
    }
}
