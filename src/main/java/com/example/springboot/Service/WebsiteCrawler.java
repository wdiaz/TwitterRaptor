package com.example.springboot.Service;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class WebsiteCrawler {
    private WebDriver webDriver;

    public WebsiteCrawler() {
        System.setProperty("webdriver.chrome.driver", "/Users/wdiaz/Downloads/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--enable-features=ReaderMode");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.addArguments("–start-maximized");
        webDriver = new ChromeDriver(chromeOptions);


    }

    public void crawl(String url) {
        try {
            webDriver.navigate().to(url);
            Thread.sleep(5000);
            File src = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File("./test-image.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void close() {
        webDriver.close();
    }
}
