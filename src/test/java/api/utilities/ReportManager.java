package api.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String reportDir = System.getProperty("user.dir") + "/reports/APIReports";
            new File(reportDir).mkdirs(); // Create directory if not exist
            String reportPath = reportDir + "/ExtentReport_" + timeStamp + ".html";
            createInstance(reportPath);
        }
        return extent;
    }

    private static ExtentReports createInstance(String fileName) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileName);
        sparkReporter.config().setDocumentTitle("API Automation Test Report");
        sparkReporter.config().setReportName("Rest-Assured Test Results");
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Load environment/system info from ConfigManager
        extent.setSystemInfo("Project", ConfigManager.getProperty("projectName"));
        extent.setSystemInfo("Tester", ConfigManager.getProperty("testerName"));
        extent.setSystemInfo("Environment", ConfigManager.getProperty("env"));

        return extent;
    }
}
