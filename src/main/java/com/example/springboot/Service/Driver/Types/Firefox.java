package com.example.springboot.Service.Driver.Types;

import com.example.springboot.Controller.SeleniumController;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Firefox implements IType {
    private static final int MAX_NUM_SCREENSHOTS = 4;
    private final Logger logger;
    private final Resource resource;
    Map<File, Integer> fileMap;
    private WebDriver driver;

    public Firefox() {
        logger = LoggerFactory.getLogger(SeleniumController.class);
        resource = new ClassPathResource("geckodriver");
        fileMap = new HashMap<>();
    }

    public Boolean shoot(String url) throws IOException, InterruptedException {
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
        int fileIndex = 1;
        if (driver instanceof FirefoxDriver) {
            if (isScrollBarPresent) {
                while (scrollHeight > 0 && fileIndex <= MAX_NUM_SCREENSHOTS) {
                    Thread.sleep(1000);
                    File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(srcFile, new File("/tmp/image" + fileIndex + ".jpg"));
                    fileMap.put(new File("/tmp/image" + fileIndex + ".jpg"), fileIndex);
                    js.executeScript("window.scrollTo(0," + clientHeight * fileIndex++ + ")");
                    scrollHeight = scrollHeight - clientHeight;
                }
            } else {
                Thread.sleep(1000);
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(srcFile, new File("/tmp/image.jpg"));
                fileMap.put(new File("/tmp/image.jpg"), fileIndex);
            }
        }
        return true;
    }

    public Map getFileMap() {
        return fileMap;
    }

    public void close() {
        driver.close();
    }

    public void quit() {
        driver.quit();
    }
}
