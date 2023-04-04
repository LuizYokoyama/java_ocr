package com.ly.ocr;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class OcrApplication {

	public static WebDriver webDriver;



	public static void main(String[] args) {

		String chromeDriverPath = "chromedriver_linux64/chromedriver" ;
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--silent");
		webDriver = new ChromeDriver(options);

		SpringApplication.run(OcrApplication.class, args);

	}


}
