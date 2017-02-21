import java.util.concurrent.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.*;

public class WAITS {

	public static void main(String[] args) {
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.amazon.com");

		// BUILT IN WAITS

		WebElement myDynamicElement = (new WebDriverWait(driver, 10)) // will
																		// wait
																		// for
																		// up to
																		// 10
																		// sec
				.until(ExpectedConditions.presenceOfElementLocated(By.id("myDynamicElement")));

		// Sometimes WebElement, list of WE, boolean, it depends on the Expected
		// Condition and what it will return

		Boolean doesURLMatch = (new WebDriverWait(driver, 10)) // will wait for
																// up to 10 sec
				.until(ExpectedConditions.urlMatches("regex"));

		// FUILD, CUSTOMIZABLE WAITS

		// waiting for element to appear for 30 sec, checking for it every 2
		// sec.
		//
		Wait wait = new FluentWait(driver).withTimeout(30, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);

		WebElement foo = wait.until(new Function() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("foo"));
			}
		});
	}

}
