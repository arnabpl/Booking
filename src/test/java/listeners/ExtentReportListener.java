package listeners;



import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;

    private static final ThreadLocal<ExtentTest> TEST =
            new ThreadLocal<>();

    static {
        new File("test-output").mkdirs();

        ExtentSparkReporter spark =
                new ExtentSparkReporter(
                        "test-output/ExtentReport.html");

        spark.config().setDocumentTitle(
                "Reservation Hub API Automation");

        spark.config().setReportName(
                "API Regression Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Language", "Java 17");
        extent.setSystemInfo("API Framework", "REST Assured");
        extent.setSystemInfo("Test Framework", "TestNG");
        extent.setSystemInfo("Build Tool", "Maven");
    }

    @Override
    public void onTestStart(ITestResult result) {

        ExtentTest test = extent.createTest(
                result.getMethod().getMethodName());

        for (String group : result.getMethod().getGroups()) {
            test.assignCategory(group);
        }

        TEST.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TEST.get().pass("Test passed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        TEST.get().fail(result.getThrowable());

        TEST.get().fail(
                "Failure details: " +
                        result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        if (TEST.get() == null) {
            TEST.set(extent.createTest(
                    result.getMethod().getMethodName()));
        }

        TEST.get().skip("Test skipped: " +
                (result.getThrowable() == null
                        ? "Execution skipped"
                        : result.getThrowable().getMessage()));
    }

    public static void log(String message) {

        if (TEST.get() != null) {
            TEST.get().info(message);
        }
    }

    @Override
    public void onFinish(
            org.testng.ITestContext context) {

        extent.flush();
        TEST.remove();
    }
}
