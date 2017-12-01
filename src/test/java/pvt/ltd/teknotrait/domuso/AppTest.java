package pvt.ltd.teknotrait.domuso;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTest {

	public static WebDriver wd = null;

	String browser = "saucelabs";

	public static final String USERNAME = "Tapas";// "Abhik";
	public static final String ACCESS_KEY = "f606712c-9be1-44f6-b9b0-05041456ed9b";// "fc847338-da32-4d2a-b3bf-64774791e545";
	protected Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@SuppressWarnings("deprecation")
	@BeforeTest
	public void setUp() throws Exception {

		String appURL = "https://app.domuso.com/renter/signup";
		String os = System.getProperty("os.name");

		if (browser.equalsIgnoreCase("firefox")) {

			if (os.equals("Linux")) {

				System.setProperty("webdriver.gecko.driver",
						System.getProperty("user.dir") + File.separator + "lib" + File.separator + "geckodriver");

			} else if (os.startsWith("Windows")) {

				System.setProperty("webdriver.gecko.driver",
						System.getProperty("user.dir") + File.separator + "lib" + File.separator + "geckodriver.exe");

			}

			FirefoxProfile ffprofile = new FirefoxProfile();
			ffprofile.setPreference("dom.webnotifications.enabled", false);

			wd = new FirefoxDriver(ffprofile);

		} else if (browser.equalsIgnoreCase("chrome")) {

			if (os.equals("Linux")) {

				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + File.separator + "lib" + File.separator + "chromedriver");

			} else if (os.startsWith("Windows")) {

				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + File.separator + "lib" + File.separator + "chromedriver.exe");

			}

			ChromeOptions options = new ChromeOptions();

			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			options.setExperimentalOption("prefs", prefs);

			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			options.addArguments("start-maximized");
			options.addArguments("--disable-notifications");

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);

			wd = new ChromeDriver(capabilities);

		} else if (browser.equalsIgnoreCase("saucelabs")) {

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability("platform", "Windows 8.1");
			capabilities.setCapability("browserName", "chrome");
			capabilities.setCapability("chromedriverVersion", "2.32");
			capabilities.setCapability("version", "latest");
			capabilities.setCapability("seleniumVersion", "3.52");

			capabilities.setCapability("name", this.getClass().getSimpleName());

			final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub";

			wd = new RemoteWebDriver(new URL(URL), capabilities);

		} else {

			System.err.println("Not a complitable environmnet to run script.");
		}

		// Set Implicit wait
		wd.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		// Navigate to the app url
		wd.get(appURL);
		logger.info("Navigate to the application URL");

	}

	@Test
	public void demoTest() {

		try {

			String propertyName = "Malibu Apartments";

			String uniqueVal = getUniqueValue();
			String firstName = "Auto";
			String lastName = "Test";
			String email = "atest" + uniqueVal + "@domain.com";
			String phone = "9737561387";// getRandomPhoneNumber();
			String password = "Password@123";
			String paymentAmt = "100";
			String accountNumber = getRandomAccountNumber();

			// WebDriverWait wait = new WebDriverWait(wd, 30);

			// Enter "Malibu Apartments"
			wd.findElement(By.xpath(".//*[@id='searchPropertyForm']/div/input")).sendKeys(propertyName);

			// Select option
			List<WebElement> optionsList = wd.findElements(By.xpath(".//*[@id='ui-id-1']/li/a"));

			for (WebElement property : optionsList) {

				if ((property.getText()).equalsIgnoreCase(propertyName)) {

					property.click();
					break;
				}
			}
			logger.info("Select 'Malibu Apartments'");

			// Wait for page load
			Thread.sleep(3000);

			// Click on [Create Account] button
			wd.findElement(By.xpath("//input[@value='Create account']")).click();

			// Wait for page load
			Thread.sleep(3000);

			// Enter [First Name]
			wd.findElement(By.id("firstName")).sendKeys(firstName);

			// Enter [Last Name]
			wd.findElement(By.id("lastName")).sendKeys(lastName);

			// Enter [Email]
			wd.findElement(By.id("email")).sendKeys(email);

			Thread.sleep(1000);

			// Enter [Phone]
			wd.findElement(By.id("phone")).click();
			wd.findElement(By.id("phone")).sendKeys(phone);

			// Enter [Password]
			wd.findElement(By.id("password")).sendKeys(password);

			// Enter [Confirm Password]
			wd.findElement(By.id("confirmPassword")).sendKeys(password);

			// Click on [ I agree to Terms and Conditions ]
			wd.findElement(By.id("tac_read")).click();

			// Click on [ I agree to TCPA Consent & Privacy (optional)]
			wd.findElement(By.id("tcpa")).click();

			// Click on [Continue] button
			wd.findElement(By.id("submit")).click();

			// Wait for page Load
			Thread.sleep(2000);
			logger.info("created new resident account.");

			// Payments Page

			// Enter Payment Amount
			wd.findElement(By.id("paymentWidget")).sendKeys(paymentAmt);

			// Enter Date
			wd.findElement(By.id("paymentDatePicker")).sendKeys(getCurrentDate());

			// Hide Date picker
			wd.findElement(By.id("paymentWidget")).click();

			// Scroll Down
			scrollToPageEnd();

			// Click on [Continue] button
			wd.findElement(By.id("continueBtn")).click();

			logger.info("Enter payment options.");

			// Wait for Page Load
			Thread.sleep(3000);

			// Set Up Bank Account

			// Enter [Bank A/cRouting Number]
			wd.findElement(By.id("bankAccountRoutingNumber")).sendKeys(accountNumber);

			// Enter [Bank A/c Number]
			wd.findElement(By.id("bankAccountNumber")).sendKeys(accountNumber);

			// Re-enter Bank Account Number (for confirmation)
			wd.findElement(By.id("bankAccountNumberRepeat")).sendKeys(accountNumber);

			// Click on [Continue button]
			wd.findElement(By.id("submit")).click();

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@AfterTest
	public void tearDown() {

		wd.quit();
	}

	/**
	 * This method will return unique number based on datetimestamp
	 * 
	 * @return
	 */
	public static String getUniqueValue() {
		DateFormat dateFormat = new SimpleDateFormat("MMddHHmm");
		Date date = new Date();
		String uniqueValue = (String) dateFormat.format(date);
		return uniqueValue;
	}

	/**
	 * This method will return current date as MM/dd/YYYY format
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		Date date = new Date();
		String currentDate = (String) dateFormat.format(date);
		return currentDate;
	}

	/**
	 * This will return a random 10 digit number.
	 * 
	 * @return
	 */
	public static String getRandomPhoneNumber() {

		long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

		return Long.toString(number);
	}

	/**
	 * This method will return random A/c Number
	 * 
	 * @return
	 */
	public static String getRandomAccountNumber() {

		long number = (long) Math.floor(Math.random() * 9_000_000_00L) + 1_000_000_00L;

		return Long.toString(number);
	}

	/**
	 * This method will scroll down to a specific pixel
	 * 
	 * @param pixel
	 */
	public static void scrollDown(int pixel) {
		JavascriptExecutor jse = (JavascriptExecutor) wd;
		jse.executeScript("scroll(0, " + pixel + ");");
	}

	/**
	 * This method will scroll down to the end of the page
	 * 
	 * @param pixel
	 */
	public static void scrollToPageEnd() {

		((JavascriptExecutor) wd).executeScript("window.scrollTo(0, document.body.scrollHeight)");

	}

}
