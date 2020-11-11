package com.example.springboot.Controller;

import com.example.springboot.Application;
import com.example.springboot.Entity.Mentions;
import com.example.springboot.Repository.MentionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.*;

import java.util.List;

@RestController
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private MentionsRepository mentionsRepository;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Mentions> getAllMentions() {
        return mentionsRepository.findAll();
    }

    @GetMapping("/tweet")
    public String tweet() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        Status status = twitter.updateStatus("This Tweet was created with Twitter Raptor");
        return status.getText();
    }

    @GetMapping("timeline")
    public String timeline() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        return twitter.getMentionsTimeline().toString();
    }

    @GetMapping("/mentions")
    public String mentions() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();


        int page = 1;
        int totalTweets = page * 10;
        Paging paging = new Paging(1, totalTweets);
        List<Status> statuses = twitter.getMentionsTimeline(paging);


        int counter = 0;
        for (Status status : statuses) {
            String inReplyToScreenNAme = status.getInReplyToScreenName();
            String inReplyToStatusId = String.valueOf(status.getInReplyToStatusId());
            String tweetText = status.getText();

            logger.info("In reply to: " + status.getInReplyToStatusId());
            URLEntity[] urls = status.getURLEntities();
            for (URLEntity url : urls) {
                logger.info("URL: " + url.getURL());
            }
            logger.info(++counter + " -> " + inReplyToStatusId + " = " + inReplyToScreenNAme + "=" + tweetText);
        }
        //return statuses.toString();
        return "ok";
    }
}