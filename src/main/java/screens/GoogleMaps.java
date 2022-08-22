package screens;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utilities.Operations;

public class GoogleMaps {

	private static WebDriver driver;
	private static Operations operations;
	private static String screenshot_FolderPath ="";
	private static String excelSheet_Path ="";
	private static String configFilePath ="";

	@FindBy(how = How.XPATH, using = ".//button[@aria-label='Directions']")
	public WebElement directionsIcon;

	@FindBy(how = How.XPATH, using = ".//div[@id='directions-searchbox-0']//input")
	public WebElement source;

	@FindBy(how = How.XPATH, using = ".//div[@id='directions-searchbox-1']//input")
	public WebElement destination;

	@FindBy(how = How.XPATH, using = ".//*[@aria-label='Driving']")
	public WebElement drivingMode;

	@FindBy(how = How.ID, using = "section-directions-trip-travel-mode-0")
	public WebElement firstRoute;

	@FindBy(how = How.XPATH, using = ".//*[contains(@aria-labelledby,'directions-mode-group-title')]")
	public List<WebElement> pathContainer;
	
	@FindBy(how = How.XPATH,using =".//div[@tabindex='0' and @class='EfN6d']")
	public List<WebElement> directions;

	public GoogleMaps(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		operations = new Operations(driver);
		
		configFilePath = System.getProperty("user.dir")+"\\config.properties";
		screenshot_FolderPath = System.getProperty("user.dir") + operations.getProperty(configFilePath, "screenshot_folder_path");
		excelSheet_Path = System.getProperty("user.dir") + operations.getProperty(configFilePath, "excel_filePath");
		
	}

	public void getDirections(String sourceLocation, String destinationLocation) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		//Entering source & Destination
		
		operations.clickOnElement(directionsIcon);
		operations.sendTextElement(source, sourceLocation);
		operations.sendTextElement(destination, destinationLocation);
		
		operations.setResults(excelSheet_Path, "From "+sourceLocation+" To "+destinationLocation);
		destination.sendKeys(Keys.ENTER);
		
		// Selecting Driving Mode
		
		operations.clickOnElement(drivingMode);
		
		try {
			Thread.sleep(5000);
			firstRoute.click();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			// Code will work in both the situations i] Directions are inside containers ii] Directions displayed directly
			
			int size = pathContainer.size();
			
			if(size>1)
			{
				operations.captureScreenshot("pathContainers", screenshot_FolderPath); 
				Iterator itr = pathContainer.iterator();
				
				while(itr.hasNext()) {
					WebElement ele = (WebElement) itr.next();
					operations.clickOnElement(ele);				
					
				}
				
				itr = directions.iterator();
				operations.captureScreenshot("directions", screenshot_FolderPath); 
				while(itr.hasNext()) {
					WebElement ele = (WebElement) itr.next();
				String path = operations.getTextFromWebElement(ele);
				System.out.println(path);
				operations.setResults(excelSheet_Path, path);
				}	
			}
			else {
				Iterator itr = directions.iterator();
				operations.captureScreenshot("directions", screenshot_FolderPath); 
				
				while (itr.hasNext()) {
					WebElement ele = (WebElement) itr.next();
				String path = operations.getTextFromWebElement(ele);
				System.out.println(path);
				operations.setResults(excelSheet_Path, path);
				}
			}
			
			
		}
		catch(NoSuchElementException e) {
			// Forcefully capturing exception because some times directions are getting displayed inside expandable containers
		}

	}
}
