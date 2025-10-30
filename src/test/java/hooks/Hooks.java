package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import support.BrowserClass;
import support.ConfigReader;
import io.cucumber.java.Scenario;
import support.ScreenShot;

public class Hooks {

    private static WebDriver driver;

    @Before
    public void setUp() {
        driver = BrowserClass.openBrowser();
        driver.manage().window().maximize();

        ConfigReader cfg = new ConfigReader();
        String url = cfg.get("baseUrl") + cfg.get("articleUrl");
        System.out.println("Opening URL: " + url);
        driver.get(url);
    }

    @After
        public void tearDown(Scenario scenario) {
            if (scenario.isFailed()) {
        		try {
        			ScreenShot.ScreenShotMethod(driver, scenario.getName());
        			System.out.println("Screenshot captured for failed scenario: " + scenario.getName());
        		} catch (IOException e) {
        			System.out.println("Failed to capture screenshot: " + e.getMessage());
        		}
            }
        if (driver != null) driver.quit();
    }

    public static WebDriver getDriver() {
        return driver;
    }
}