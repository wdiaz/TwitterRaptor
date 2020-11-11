package com.example.springboot.Controller;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.io.File;

import org.apache.commons.io.FileUtils;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;


@RestController
public class SeleniumController {

    private WebDriver driver;

    @GetMapping("/capture")
    public String capture() {
        return "ok";
    }

    @RequestMapping("/screenshots")
    public String index(@RequestParam("url") String url) {
        /*WebsiteCrawler websiteCrawler = new WebsiteCrawler();
        websiteCrawler.crawl(url);*/
        System.setProperty("webdriver.chrome.driver", "/Users/wdiaz/Downloads/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--enable-features=ReaderMode");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.addArguments("–start-maximized");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(url);

        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
        try {
            BufferedImage image = screenshot.getImage();
            ImageIO.write(image, "PNG", new File("./test-image.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.quit();
        return url;
    }
}
