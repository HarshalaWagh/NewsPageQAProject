package support;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

public class BrowserClass {

	public static WebDriver openBrowser() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();

		// Anti-detection options to bypass Cloudflare
		options.addArguments("--disable-blink-features=AutomationControlled");
		options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
		options.setExperimentalOption("useAutomationExtension", false);
		
		// Set realistic user agent
		options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
		
		options.addArguments("--disable-notifications");
		options.addArguments("--start-maximized");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-infobars");
		options.addArguments("--disable-extensions");

		// Use OS temp directory (works on Windows/Mac/Linux)
		String temp = System.getProperty("java.io.tmpdir");
		options.addArguments("--user-data-dir=" + Paths.get(temp, "chrome-" + UUID.randomUUID()));

		// Headless toggle (default false so local behaves like before)
		boolean headless = Boolean.parseBoolean(System.getProperty("HEADLESS", "false"));
		if (headless) {
			options.addArguments("--headless=new", "--window-size=1920,1080");
		}

		// Harden only on Linux runners
		String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
		if (os.contains("linux")) {
			options.addArguments("--no-sandbox");
		}

		WebDriver driver = new ChromeDriver(options);
		
		// Remove webdriver property to avoid detection
		((org.openqa.selenium.chromium.ChromiumDriver) driver).executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", 
			java.util.Map.of("source", "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"));

		return driver;
	}
}
