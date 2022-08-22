package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import net.bytebuddy.dynamic.loading.ClassInjector.UsingReflection;

public class Operations {

	private static WebDriver driver;
	public Operations () {
		// default constructor
	}
	
	public Operations(WebDriver driver) {
		this.driver = driver;
	}

	public static void highlightElement(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
				"background:yellow; border:2px solid red;");
		try {
			Thread.sleep(250);
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void moveToElement(WebElement element) {
		waitForLoad(Operations.driver);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		highlightElement(driver, element);

	}

	public static void scrollToTop() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight,0)");
	}

	public static void scrollToBottom() {
		((JavascriptExecutor)

		driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
	}

	public static String getTextFromWebElement(WebElement element) {
		moveToElement(element);
		String text = element.getText();
		return text;
	}

	public static void clickOnElement(WebElement element) {
		moveToElement(element);
		element.click();
	}

	public static void sendTextElement(WebElement element, String text) {
		moveToElement(element);
		element.sendKeys(text);
	}

	public static ArrayList<String> getTextFromListOfWebElements(List<WebElement> elements) {

		ArrayList<String> listOfText = new ArrayList<String>();
		Iterator iterator = elements.iterator();
		while (iterator.hasNext()) {
			WebElement ele = (WebElement) iterator.next();
			moveToElement(ele);
			String text = ele.getText();
			listOfText.add(text);
		}
		return listOfText;
	}

	public static String getProperty(String filePath, String key) {
		String value = "";

		try {
			FileInputStream fin = new FileInputStream(filePath);

			Properties pro = new Properties();
			pro.load(fin);
			value = pro.getProperty(key);

		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return value;
	}

	public static void captureScreenshot(String fileName, String path) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmm");
		LocalDateTime now = LocalDateTime.now();
		// System.out.println(dtf.format(now));

		TakesScreenshot ss = ((TakesScreenshot) Operations.driver);
		File sourceFile = ss.getScreenshotAs(OutputType.FILE);

		String filePath = path + fileName + dtf.format(now) + ".png";

		File destFile = new File(filePath);
		try {
			FileUtils.copyFile(sourceFile, destFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void untilAngularFinishHttpCalls() {
		final String javaScriptToLoadAngular = "var injector = window.angular.element('body').injector();"
				+ "var $http = injector.get('$http');" + "return ($http.pendingRequests.length === 0)";

		ExpectedCondition<Boolean> pendingHttpCallsCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(javaScriptToLoadAngular).equals(true);
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 20); // timeout = 20 secs
		wait.until(pendingHttpCallsCondition);
	}

	
	public void setResults(String filePath, String parameter) {

		FileInputStream fin = null;
		Row row = null;
		Cell cell = null;
		try {
			fin = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			 Workbook wb = new XSSFWorkbook(fin);
			// wb.set(workbook);

			Sheet sheet = wb.getSheetAt(0);
			int rowNum = sheet.getLastRowNum()+1;
			
			row = sheet.getRow(rowNum);
			if(row == null) {
				sheet.createRow(rowNum);
				row = sheet.getRow(rowNum);
			}
			
			cell = row.createCell(0);
			cell.setCellValue(parameter);		
			
			fin.close();

			FileOutputStream fout = new FileOutputStream(filePath);
			wb.write(fout);
			fout.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);

}
}