package com.example.springboot.Controller;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class SeleniumController {

    private WebDriver driver;

    private Logger logger;

    @RequestMapping("/ss")
    public String ss(@RequestParam("url") String url) throws IOException {
        System.setProperty("webdriver.chrome.driver", "/Users/wdiaz/Downloads/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--flag-switches-begin");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.addArguments("–start-maximized");
        //chromeOptions.addArguments("--enable-features=ReaderMode");
        chromeOptions.addArguments("--flag-switches-end");


        Map<String, String> mobileEmulation = new HashMap<>();

        mobileEmulation.put("deviceName", "Galaxy S5");

        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get(url);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        JavascriptExecutor jexec = (JavascriptExecutor) driver;
        jexec.executeScript("window.scrollTo(0,0)"); // will scroll to (0,0) position
        boolean isScrollBarPresent = (boolean) jexec.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight");
        long scrollHeight = (long) jexec.executeScript("return document.documentElement.scrollHeight");
        long clientHeight = (long) jexec.executeScript("return document.documentElement.clientHeight");
        int fileIndex = 1;
        if (driver instanceof ChromeDriver) {
            if (isScrollBarPresent) {
                while (scrollHeight > 0) {
                    File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    org.apache.commons.io.FileUtils.copyFile(srcFile, new File("./test-image" + fileIndex + ".jpg"));
                    jexec.executeScript("window.scrollTo(0," + clientHeight * fileIndex++ + ")");
                    scrollHeight = scrollHeight - clientHeight;
                }
            } else {
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                org.apache.commons.io.FileUtils.copyFile(srcFile, new File("./test-image.jpg"));
            }
        }
        driver.close();
        driver.quit();
        return "ok";
    }
}
