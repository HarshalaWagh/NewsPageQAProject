package support;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.UUID;

public class BrowserClass {

    public static WebDriver openBrowser() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        options.addArguments(
                "--disable-notifications",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1366,900",
                "--user-data-dir=/tmp/chrome-" + UUID.randomUUID()
        );

        if (Boolean.parseBoolean(System.getProperty("HEADLESS", "true"))) {
            options.addArguments("--headless=new");
        }

        return new ChromeDriver(options);
    }
}
