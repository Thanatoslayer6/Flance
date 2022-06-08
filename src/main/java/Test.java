import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Test {
	public static WebDriver driver;
	public static WebElement totype, confirm, startButton;
	public static String word;

	public static void login(Credentials creds) {
		// Enter mail
		driver.get("https://freelancesage.com/oauth/google");
		WebElement l = driver.findElement(By.name("identifier"));
		l.sendKeys(creds.getEmail());
		driver.findElement(By.id("identifierNext")).click();
		// Password
		WebElement p = new WebDriverWait(driver, Duration.ofSeconds(5))
				.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
		p.sendKeys(creds.getPwd());
		driver.findElement(By.id("passwordNext")).click();
		new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.urlToBe("https://freelancesage.com/dashboard"));
	}
	public static void main(String[] args) {
        Credentials creds = new Credentials(); // Ask user for credentials by constructing class
        
		WebDriverManager.chromedriver().setup();
		/*
		ChromeOptions opts = new ChromeOptions();
		opts.setHeadless(true);
		opts.addArguments("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.61 Safari/537.36");
		driver = new ChromeDriver(opts);
		*/
		driver = new ChromeDriver();
		login(creds);
		for (int k = 0; k < 50; k++) {
			if (k == 0) {
				driver.get("https://freelancesage.com/play");
			} else {
				driver.navigate().refresh();
			}
			// Wait for start button to show up
			startButton = new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(ExpectedConditions.elementToBeClickable(By.id("start-btn")));
			System.out.println("Start Button has Appeared!");
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].click();", startButton);
			System.out.println("Start Button has been clicked!");
			// END
			// Wait for text to display
			word = getWord(""); // Get the text store into String variable

			for (int i = 0;; i++) {
				if (!driver.findElements(By.className("swal2-confirm")).isEmpty()) {
					System.out.println("It is done!");
					break;
				}
				if (i >= 1) {
					word = getWord(word);
				}
				System.out.println("The text is: " + word);
				sendKeys(word, driver.findElement(By.id("quoteInput"))); // Write the word

			}
		}

		exit();
        
	}

	public static String getWord(String dw) {
		try {
			if (dw.equals("")) {
			totype = new WebDriverWait(driver, Duration.ofSeconds(3))
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("quoteDisplay")));
			} else {
				// We wait for now
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
				wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(totype, dw)));
			}		
		} catch (TimeoutException ignored) {
			return "";
		}
		try {
			// After we wait just grab text again
			totype = new WebDriverWait(driver, Duration.ofSeconds(3))
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("quoteDisplay")));
			return totype.getText();		
		} catch (TimeoutException ignored) {
			return "";
		}

	}

	public static void sendKeys(String keysToSend, WebElement element) {
		for (char c : keysToSend.toCharArray()) {
			try {
				element.sendKeys(Character.toString(c));
				driver.manage().timeouts().implicitlyWait(Duration.ofMillis(30));
			} catch (NoSuchElementException ignored) {
				System.out.println("Cannot type anymore so just qutting now...");
				break;
			} catch (ElementNotInteractableException ignored) {
				System.out.println("Cannot interact with element, quitting loop now...");
				break;
			}
		}
	}

	public static void exit() {
		driver.close();
		driver.quit();
	}

}
