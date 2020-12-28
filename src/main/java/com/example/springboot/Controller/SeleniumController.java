package com.example.springboot.Controller;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class SeleniumController {

    private WebDriver driver;

    private Logger logger;

    @RequestMapping("/ss")
    public String ss(@RequestParam("url") String url) throws IOException {
        logger = LoggerFactory.getLogger(SeleniumController.class);
        Resource resource = new ClassPathResource("chromedriver");
        InputStream input = resource.getInputStream();
        File targetDriver = resource.getFile();
        System.setProperty("webdriver.chrome.driver", targetDriver.toString());

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--flag-switches-begin");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.addArguments("–start-maximized");
        chromeOptions.addArguments("--enable-features=ReaderMode");
        chromeOptions.addArguments("--flag-switches-end");

        Map<String, String> mobileEmulation = new HashMap<>();

        //mobileEmulation.put("deviceName", "Galaxy S5");
        mobileEmulation.put("deviceName", "iPhone X");
        //mobileEmulation.put("deviceName", "iPad");

        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

        WebDriver driver = new ChromeDriver(chromeOptions);
        //driver.manage().window().setPosition(new Point(0,0));
        //driver.manage().window().setSize(new Dimension(1024,768));
        driver.get(url);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        JavascriptExecutor jexec = (JavascriptExecutor) driver;
        jexec.executeScript("window.scrollTo(0,0)"); // will scroll to (0,0) position
        boolean isScrollBarPresent = (boolean) jexec.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight");
        long scrollHeight = (long) jexec.executeScript("return document.documentElement.scrollHeight");
        long clientHeight = (long) jexec.executeScript("return document.documentElement.clientHeight");
        int fileIndex = 1;

        logger.info("Client Height: " + clientHeight);
        logger.info("Scroll Height: " + scrollHeight);
        clientHeight = clientHeight - (long) (clientHeight * 0.04);
        logger.info("New Client Height: " + clientHeight);
        if (driver instanceof ChromeDriver) {
            if (isScrollBarPresent) {
                while (scrollHeight > 0 && fileIndex <= 6) {
                    File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(srcFile, new File("/tmp/test-image" + fileIndex + ".jpg"));
                    int scrollTo = (int) clientHeight * fileIndex++;
                    jexec.executeScript("window.scrollTo(0," + scrollTo + ")");
                    scrollHeight = scrollHeight - clientHeight;
                    logger.info("New scroll to: " + scrollTo);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        logger.info(ex.getMessage());
                    }
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
    public String ff(@RequestParam("url") String url) throws IOException, InterruptedException {
        logger = LoggerFactory.getLogger(SeleniumController.class);
        Resource resource = new ClassPathResource("geckodriver");
        InputStream input = resource.getInputStream();
        File targetDriver = resource.getFile();
        System.setProperty("webdriver.gecko.driver", targetDriver.toString());
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("Marionette", true);
        firefoxOptions.merge(capabilities);
        firefoxOptions.setProfile(firefoxProfile);
        firefoxOptions.setHeadless(true);
        WebDriver driver = new FirefoxDriver(firefoxOptions);
        driver.manage().window().maximize();
        Dimension size = driver.manage().window().getSize();
        Dimension d = new Dimension(600, 1400); //size.getHeight());
        driver.manage().window().setSize(d);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        driver.get("about:reader?url=" + url);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Thread.sleep(1000);
        boolean isScrollBarPresent = (boolean) js.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight");
        long scrollHeight = (long) js.executeScript("return document.documentElement.scrollHeight");
        long clientHeight = (long) js.executeScript("return document.documentElement.clientHeight");
        //logger.info("document.documentElement.scrollHeight: " + scrollHeight );
        //logger.info("document.documentElement.clientHeight: " + clientHeight);
        int fileIndex = 1;
        if (driver instanceof FirefoxDriver) {
            if (isScrollBarPresent) {
                while (scrollHeight > 0) {
                    Thread.sleep(1000);
                    File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(srcFile, new File("./test-image" + fileIndex + ".jpg"));
                    js.executeScript("window.scrollTo(0," + clientHeight * fileIndex++ + ")");
                    scrollHeight = scrollHeight - clientHeight;
                }
            } else {
                Thread.sleep(1000);
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(srcFile, new File("./test-image.jpg"));
            }
        }
        return "ok";
    }
}
