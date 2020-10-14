package com.example.springboot.Controller;

import com.example.springboot.Service.WebsiteCrawler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SeleniumController {

    @GetMapping("/capture")
    public String capture() {
        return "ok";
    }

    @RequestMapping("/screenshots")
    public String index(@RequestParam("url") String url) {
        WebsiteCrawler websiteCrawler = new WebsiteCrawler();
        websiteCrawler.crawl(url);
        return url;
    }
}
