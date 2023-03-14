package caseStudy;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import commonUtils.Utility;
import io.github.bonigarcia.wdm.WebDriverManager;


public class DemoBlazeTest {
	public class MiniCaseStudy {
		WebDriver driver;
		ExtentReports reports;
		ExtentSparkReporter spark;
		ExtentTest extentTest;
		
		@BeforeTest(groups = "Run@Time")
		public void setup() throws IOException {
		    WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
			
			reports = new ExtentReports();
			spark = new ExtentSparkReporter("target\\DemoBlazeReport.html");
			reports.attachReporter(spark);
		}
		
	    @Test(priority=1,groups = "Run@Time")
	    public void logIn() {
	    	extentTest = reports.createTest("LoginPageTest");
	    	driver.get("https://www.demoblaze.com/");
	    	driver.findElement(By.id("login2")).click();
	    	driver.findElement(By.id("loginusername")).sendKeys("ShriAthi");
	    	driver.findElement(By.id("loginpassword")).sendKeys("Shri.01");
	    	driver.findElement(By.xpath("//button[text()='Log in']")).click();
//	    	boolean isDisp = driver.findElement(By.xpath("//a[text()='Welcome ShriAthi']")).isDisplayed();
//	    	String Disp = driver.findElement(By.xpath("//a[text()='Welcome ShriAthi']")).getText();
//	    	Assert.assertTrue(isDisp);
//	    	System.out.println(Disp);
	    }
	    @Test(priority=2)
	    public void addItem() throws InterruptedException {
	    	
	    	extentTest = reports.createTest("AddItemTest");
	    	Thread.sleep(3000);
	    	driver.findElement(By.xpath("//a[text()='Phones']")).click();
	    	driver.findElement(By.xpath("//a[text()='Nokia lumia 1520']")).click();
	    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    	WebElement btn = driver.findElement(By.xpath("//a[text()='Add to cart']"));
			wait.until(ExpectedConditions.elementToBeClickable(btn));
			btn.click();
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.accept();
//			System.out.println(alert.getText());

	    }
	    @Test(priority=3,dataProvider="MultipleData",groups = "Run@Time")
	    public void manyItem(String Product) throws InterruptedException{
	    	
	    	extentTest = reports.createTest("AddMultipleItemTest");
	    	Thread.sleep(2000);
	    	driver.navigate().to("https://www.demoblaze.com/index.html");
	    	Thread.sleep(2000);
	    	driver.findElement(By.xpath("//a[text()='Phones']")).click();
	    	Thread.sleep(2000);
	    	driver.findElement(By.linkText(Product)).click();
	    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    	WebElement btn1 = driver.findElement(By.xpath("//a[text()='Add to cart']"));
			wait.until(ExpectedConditions.elementToBeClickable(btn1));
			btn1.click();
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.accept();
			driver.findElement(By.xpath("//a[contains(text(),'Cart')]")).click();
			
	    }
	    
	    @Test(priority=4)
	    public void deleteItem() throws InterruptedException {
	    	
	    	extentTest = reports.createTest("DeleteItemTest");
//	    	driver.findElement(By.id("cartur")).click();
//	    	Thread.sleep(2000);
			driver.findElement(By.xpath("//a[contains(text(),'Delete')][1]")).click();
	    	Thread.sleep(2000);
	    }
	    
	    @Test(priority=5,groups = "Run@Time")
	    public void purchaseproduct() throws InterruptedException {
	    	
	    	extentTest = reports.createTest("PurchaseproductTest");
	    	Thread.sleep(2000);
	    	driver.findElement(By.xpath("//button[contains(text(),'Place Order')]")).click();
			driver.findElement(By.xpath("//input[@id='name']")).sendKeys("ShriAthi");
			driver.findElement(By.xpath("//input[@id='country']")).sendKeys("India");
			driver.findElement(By.xpath("//input[@id='city']")).sendKeys("Tirunelveli");
			driver.findElement(By.xpath("//input[@id='card']")).sendKeys("6383 5188 3645 1758");
			driver.findElement(By.xpath("//input[@id='month']")).sendKeys("September");
			driver.findElement(By.xpath("//input[@id='year']")).sendKeys("2023");
	    	Thread.sleep(2000);
	    	driver.findElement(By.xpath("//button[contains(text(),'Purchase')]")).click();
	    	Thread.sleep(2000);
	    	driver.findElement(By.xpath("//button[contains(text(),'OK')]")).click();
	    }
	    
	    @DataProvider(name="MultipleData")								
	    public Object[][] getData() throws CsvValidationException, IOException{
	    	  String path = System.getProperty("user.dir")+"//src//main//resources//testData//demoBlaze.csv";
	    	  String[] cols;
	  	  CSVReader reader = new CSVReader(new FileReader(path));
	  	  ArrayList<Object> dataList = new ArrayList<Object>();
	  	  while((cols = reader.readNext())!=null) {
	  		  Object[] record = {cols[0]};
	  		  dataList.add(record);
	  	  }
	  	  return dataList.toArray(new Object[dataList.size()][]);	  
	  }
	       
	    @AfterMethod(groups = "Run@Time")
		 public void tearDown(ITestResult result) {
			 if(ITestResult.FAILURE == result.getStatus()) {
				 extentTest.log(Status.FAIL, result.getThrowable().getMessage());
				 String strPath = Utility.getScreenshotPath(driver);
				 extentTest.addScreenCaptureFromPath(strPath);
			 }
		 }
	 
	@AfterTest
    public void finishExtent() {
	   reports.flush();
   }
	
}	
	
}

