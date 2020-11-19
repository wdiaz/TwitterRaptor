package com.example.springboot.Controller;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    @RequestMapping("ff")
    public String ff(@RequestParam("url") String url) throws IOException {
        logger = LoggerFactory.getLogger(SeleniumController.class);




        System.setProperty("webdriver.gecko.driver", "/Users/wdiaz/Downloads/geckodriver");

        /*String userAgent = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16";
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("general.useragent.override", userAgent);*/

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        //firefoxOptions.setProfile(firefoxProfile);

        //firefoxOptions.setHeadless(true);

        WebDriver driver = new FirefoxDriver(firefoxOptions);


        Dimension size = driver.manage().window().getSize();
        logger.info("Window Height: " + size.getHeight());
        logger.info("Window Width: " + size.getWidth());

        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //driver.get("about:reader?url=" + url);
        driver.get(url);


        JavascriptExecutor js = (JavascriptExecutor) driver;
        boolean isScrollBarPresent = (boolean) js.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight");
        long scrollHeight = (long) js.executeScript("return document.documentElement.scrollHeight");
        long clientHeight = (long) js.executeScript("return document.documentElement.clientHeight");

        logger.info("isScrollBarPresent: " + isScrollBarPresent);
        logger.info("scrollHeight: " + scrollHeight);
        logger.info("clientHeight" + clientHeight);

       double totalHeight = scrollHeight - (scrollHeight * 0.40);

        //js.executeScript("window.scrollTo(0, " +totalHeight + ")");
        js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight)");
        //js.executeScript("window.scrollTo(0, 500)");


        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            this.logger.info(ex.getMessage());
        }

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("./test-image.jpg"));

        return "ok";
    }
}
