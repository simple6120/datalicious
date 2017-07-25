package com.datalicious.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SearchUtility {

	private static final int WAIT = 60;// sec;
	private static final String URL = "https:\\www.google.com";

	public static String getURLFromGoogleSearch(String searchKeyWord) {
		

		ChromeDriver driver = new ChromeDriver();
		String url = "";

		try {

			driver.get(URL);

			driver.manage().timeouts().implicitlyWait(WAIT, TimeUnit.SECONDS);

			driver.findElementById("lst-ib").sendKeys(searchKeyWord);

			driver.findElementById("_fZl").click();

			driver.manage().timeouts().implicitlyWait(WAIT, TimeUnit.SECONDS);

			List<WebElement> links = driver.findElements(By.xpath("//*[@id=\"rso\"]/div/div/div[1]/div/div/h3/a"));

			for (WebElement link : links) {
				url = link.getAttribute("href");
				break;
			}

		} catch (Exception e) {
			System.out.println("Error in getURLFromGoogleSearch() method !!" + e.getMessage());
		} finally {
			if (driver != null) {
				driver.quit();
			}
		}

		return url;
	}
}
