package tests;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import screens.GoogleMaps;
import utilities.Operations;

public class BaseClass {

public static WebDriver driver;
public static Operations operations;
public static GoogleMaps googleMaps;
public static String configFilePath ="";
	
@BeforeSuite
public void setupBrowser() {
	
	configFilePath = System.getProperty("user.dir")+"\\config.properties";
	
	//===================================== SETTING UP CHROME OPTIONS ==========================================
	
	ChromeOptions options = new ChromeOptions();
	options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
	options.setExperimentalOption("useAutomationExtension", false);
	options.addArguments("--disable-extensions");
	options.addArguments("test-type");
	Map<String, Object> prefs = new HashMap<String, Object>();
	prefs.put("credentials_enable_service", false);
	prefs.put("profile.password_manager_enabled", false);
	options.setExperimentalOption("prefs", prefs);
	
	// ==================================== INITIALIZING DRIVER =================================================
	
	System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
	driver = new ChromeDriver(options);
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	driver.manage().window().maximize();
	
	operations = new Operations(driver);
	String url = operations.getProperty(configFilePath, "url");
	
	driver.get(url);
	googleMaps = new GoogleMaps(driver);

}

@AfterSuite
public void closeDriver() {
	driver.close();
	driver.quit();
}
 
}
