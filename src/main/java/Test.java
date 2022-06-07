import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Test {
	public static WebDriver driver;
	public static WebElement totype, confirm, startButton;
	public static String word;

	public static void login(String gmail, String pwd) {
		// Enter mail
		driver.get("https://accounts.google.com/ServiceLogin?elo=1");
		WebElement l = driver.findElement(By.name("identifier"));
		l.sendKeys(gmail);
		driver.findElement(By.id("identifierNext")).click();
	    // Password
	    WebElement p = new WebDriverWait(driver, Duration.ofSeconds(5))
			.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
	    p.sendKeys(pwd);
	    driver.findElement(By.id("passwordNext")).click();
        try {
            Thread.sleep(3000);
        } catch(Exception e){
            e.printStackTrace();
        }
	}

	public static void main(String[] args) {
		// System.setProperty("webdriver.chrome.driver", "./chromedriver");
        WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		// login();
        for (int k = 0; k < 10; k++) {
            if (k == 0) {
                driver.get("https://freelancesage.com/play");
            } else {
                driver.navigate().refresh();
            }
            // Wait for start button to show up
            startButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.id("start-btn")));
            System.out.println("Button has Appeared!");
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].click();", startButton);
            System.out.println("Button has been clicked!");
            // END
            // Wait for text to display
            word = getWord(""); // Get the text store into String variable
                                // driver.findElement(By.className(".swal2-confirm")).isDisplayed();
            for (int i = 0;; i++) {
                if (i >= 1) { // Skip the very first loop
                    word = getWord(word);
                }
                System.out.println("The text is: " + word);
                sendKeys(word, driver.findElement(By.id("quoteInput"))); // Write the word
                if (driver.findElements(By.className("swal2-confirm")).size() > 0) { // Check if true
                    System.out.println("It is done!");
                    break;
                }
            }
        }
		
		exit();
		
	}

	static public String getWord(String dw) {
		if (dw.equals("")) {
			totype = new WebDriverWait(driver, Duration.ofSeconds(100))
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("quoteDisplay")));
		} else {
			// We wait for now
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
			wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(totype, dw)));
		}
		// After we wait just grab text again
		totype = new WebDriverWait(driver, Duration.ofSeconds(5))
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("quoteDisplay")));
		return totype.getText();
	}

	static public void sendKeys(String keysToSend, WebElement element) {
		for (char c : keysToSend.toCharArray()) {
			element.sendKeys(Character.toString(c));
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static public void exit() {
		driver.close();
		driver.quit();
	}

}
