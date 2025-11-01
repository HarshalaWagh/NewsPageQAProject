package support;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class ScreenShot {

	public static void ScreenShotMethod(WebDriver driver, String name) throws IOException {
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		
		// Use project root directory to create Screenshots folder
		String projectRoot = System.getProperty("user.dir");
		File screenshotsDir = new File(projectRoot, "Screenshots");
		
		// Create directory if it doesn't exist
		if (!screenshotsDir.exists()) {
			screenshotsDir.mkdirs();
		}
		
		// Screenshot filename to remove invalid characters for file system
		String screenshotName = name.replaceAll("[^a-zA-Z0-9._-]", "_");
		File dest = new File(screenshotsDir, screenshotName + ".png");
		
		FileHandler.copy(source, dest);
		System.out.println("Screenshot saved to: " + dest.getAbsolutePath());
	}
}
