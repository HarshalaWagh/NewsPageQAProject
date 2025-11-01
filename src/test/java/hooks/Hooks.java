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
		
		// Only maximize in non-headless mode (headless already has window size set)
		boolean headless = Boolean.parseBoolean(System.getProperty("HEADLESS", "false"));
		if (!headless) {
			driver.manage().window().maximize();
		}

		ConfigReader cfg = new ConfigReader();
		String url = cfg.get("baseUrl") + cfg.get("articleUrl");
		System.out.println("Opening URL: " + url);
		driver.get(url);
		
		// Wait for Cloudflare to validate and redirect to actual page
		// Check if we're on a challenge page and wait for it to complete
		try {
			Thread.sleep(5000); // Give Cloudflare time to validate
			
			// Wait for page title to change from "Just a moment..."
			org.openqa.selenium.support.ui.WebDriverWait wait = 
				new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15));
			wait.until(d -> !d.getTitle().toLowerCase().contains("just a moment"));
			
			System.out.println("Page loaded. Title: " + driver.getTitle());
		} catch (Exception e) {
			System.out.println("Page load wait completed with: " + e.getMessage());
		}
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
		if (driver != null)
			driver.quit();
	}

	public static WebDriver getDriver() {
		return driver;
	}
}