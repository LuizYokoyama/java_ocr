package com.ly.ocr;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
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

		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(new FirefoxProfile());
		options.setHeadless(true);


		String firefoxDriverPath = "geckodriver/geckodriver" ;
		System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
		webDriver = new FirefoxDriver(options);

	}


}
