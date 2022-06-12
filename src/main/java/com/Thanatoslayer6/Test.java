package com.Thanatoslayer6;
import java.time.Duration;
import java.util.Collections;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Test {
	public static WebDriver driver;
	public static WebElement totype, confirm, startButton, points, energy;
	public static String word;

	public static void login(CredentialsLogin creds) {
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
        CredentialsLogin creds = new CredentialsLogin(); // Ask user for credentials by constructing class
        
		WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // Fixing 255 Error crashes
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Options to trick bot detection
        // Removing webdriver property
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", null);

        // Changing the user agent / browser fingerprint
		options.addArguments("user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.61 Safari/537.36");
        // Other
        options.addArguments("disable-infobars");
        options.addArguments("--headless"); // Try headless

		driver = new ChromeDriver(options);
        // Login then go to '/play' section
		login(creds);
		driver.get("https://freelancesage.com/play");
		// Find energy selector by calling method
		energy = getEnergyHandle();
		int energy_int = Integer.parseInt(energy.getText().replaceAll("\\D+", ""));
		// End
		for (int k = 0; k < energy_int; k++) {
			if (k == 0) {
				// Show the amount of energy
				System.out.println(energy.getText());
				System.out.println("===== Starting the game now =====");
			} else {
				// Refresh the page to play again...
				driver.navigate().refresh();
				// Show the amount of energy
				energy = getEnergyHandle();
				System.out.println(energy.getText());
				System.out.println("===== Starting the game now =====");
			}
			// Wait for start button to show up
			startButton = new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(ExpectedConditions.elementToBeClickable(By.id("start-btn")));
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].click();", startButton);
			// Wait for text to display
			word = getWord(""); // Get the text store into String variable

			for (int i = 0;; i++) {
				if (!driver.findElements(By.className("swal2-confirm")).isEmpty()) {
                    confirm = new WebDriverWait(driver, Duration.ofSeconds(10))
                    		.until(ExpectedConditions.elementToBeClickable(By.className("swal2-confirm")));
                    confirm.click();
                    points = new WebDriverWait(driver, Duration.ofSeconds(10))
                    		.until(ExpectedConditions.visibilityOfElementLocated(By.className("total_points")));
                    System.out.printf("~~~ Game Over! You won %s points ~~~\n", points.getText());
					break;
				}
				if (i >= 1) {
					word = getWord(word);
				}
               
                if ((i % 10) == 0) { // Output every 10th word to the console just to be sure
				    System.out.printf("[%d] - The text is: %s\n", i, word);
                }
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
		//for (char c : keysToSend.toCharArray()) {
		try {
			element.sendKeys(keysToSend);
			//driver.manage().timeouts().implicitlyWait(Duration.ofMillis(30));
		} catch (NoSuchElementException ignored) {
			System.out.println("Cannot type anymore so just qutting now...");
			//break;
		} catch (ElementNotInteractableException ignored) {
			System.out.println("Cannot interact with element, quitting loop now...");
			//break;
		}
		//}
	}
	public static WebElement getEnergyHandle() {
		return new WebDriverWait(driver, Duration.ofSeconds(10))
			.until(ExpectedConditions.visibilityOfElementLocated(By.className("energy-left")));
	}
	public static void exit() {
		driver.close();
		driver.quit();
	}

}
