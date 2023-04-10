package com.ly.ocr;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class OcrApplication {

	public static WebDriver webDriver;


	public static void main(String[] args) {
		webDriverInit();
		SpringApplication.run(OcrApplication.class, args);


	}

	public static void webDriverInit() {

		FirefoxOptions browserDriverOptions = new FirefoxOptions();
		browserDriverOptions.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--silent");
		String firefoxDriverPath = "geckodriver/geckodriver" ;
		System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
		webDriver = new FirefoxDriver(browserDriverOptions);

	}


}
