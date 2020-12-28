package com.example.springboot.Service.Driver.Types;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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

public class Chrome implements IType {

    private static final int MAX_NUM_SCREENSHOTS = 5;
    private final WebDriver driver;
    private final Logger logger;
    private final Map<File, Integer> fileMap;

    public Chrome() {
        logger = LoggerFactory.getLogger(Chrome.class);
        Resource resource = new ClassPathResource("chromedriver");
        try {
            InputStream input = resource.getInputStream();
            File targetDriver = resource.getFile();
            System.setProperty("webdriver.chrome.driver", targetDriver.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        mobileEmulation.put("deviceName", "iPhone X");
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        driver = new ChromeDriver(chromeOptions);
        fileMap = new HashMap<File, Integer>();
    }

    public Boolean shoot(String url) throws IOException, InterruptedException {
        driver.get(url);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        JavascriptExecutor jexec = (JavascriptExecutor) driver;
        jexec.executeScript("window.scrollTo(0,0)"); // will scroll to (0,0) position
        boolean isScrollBarPresent = (boolean) jexec.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight");
        long scrollHeight = (long) jexec.executeScript("return document.documentElement.scrollHeight");
        long clientHeight = (long) jexec.executeScript("return document.documentElement.clientHeight");
        int fileIndex = 1;
        clientHeight = clientHeight - (long) (clientHeight * 0.04);
        if (driver instanceof ChromeDriver) {
            if (isScrollBarPresent) {
                while (scrollHeight > 0 && fileIndex <= 4) {
                    File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    String filename = "/tmp/image" + fileIndex + ".jpg";
                    fileMap.put(new File("/tmp/image" + fileIndex + ".jpg"), fileIndex);
                    FileUtils.copyFile(srcFile, new File(filename));
                    int scrollTo = (int) clientHeight * fileIndex++;
                    jexec.executeScript("window.scrollTo(0," + scrollTo + ")");
                    scrollHeight = scrollHeight - clientHeight;
                    logger.info("New scroll to: " + scrollTo);
                    Thread.sleep(1000);
                }
            } else {
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(srcFile, new File("/tmp/image.jpg"));
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
