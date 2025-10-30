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

        options.addArguments("--disable-notifications");

        // Use OS temp directory (works on Windows/Mac/Linux)
        String tmp = System.getProperty("java.io.tmpdir");
        options.addArguments("--user-data-dir=" + Paths.get(tmp, "chrome-" + UUID.randomUUID()));

        // Headless toggle (default false so local behaves like before)
        boolean headless = Boolean.parseBoolean(System.getProperty("HEADLESS", "false"));
        if (headless) {
            options.addArguments("--headless=new", "--window-size=1366,900");
        }

        // Harden only on Linux runners
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (os.contains("linux")) {
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        }

        return new ChromeDriver(options);
    }
}
