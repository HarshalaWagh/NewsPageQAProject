package support;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class ScreenShot {
	
	public static void ScreenShotMethod(WebDriver driver,String name) throws IOException{
		
		File source=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		File dest=new File("C:\\Users\\harsh\\eclipse-workspace\\KiteZerodha\\Screenshots\\"+name+".png");
		FileHandler.copy(source,dest) ;
	}
}
