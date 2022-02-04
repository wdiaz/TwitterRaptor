package com.example.springboot.Controller;

import com.example.springboot.Application;
import com.example.springboot.Entity.Mention;
import com.example.springboot.Repository.MentionRepository;
import com.example.springboot.Service.Driver.DriverFactory;
import com.example.springboot.Service.Driver.Types.IType;
import com.example.springboot.Service.MentionService;
import org.openqa.selenium.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import twitter4j.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private MentionRepository mentionsRepository;

    @Autowired
    private MentionService mentionService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Mention> getAllMentions() {
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
    public String mentions(@RequestParam("type") String type) throws TwitterException, IOException, InterruptedException {
        Twitter twitter = TwitterFactory.getSingleton();

        int page = 1;
        int totalTweets = page * 70;
        Paging paging = new Paging(1, totalTweets);
        List<Status> mentions = twitter.getMentionsTimeline(paging);
        IType driver;
        switch (type) {
            case "chrome":
                driver = DriverFactory.create("chrome");
                break;
            case "firefox":
                driver = DriverFactory.create("firefox");
                break;
            default:
                throw new InvalidArgumentException("Invalid Webdriver");
        }

        for (Status mention : mentions) {

            Long requestorTweetId = mention.getId();
            Long targetTweetId = mention.getInReplyToStatusId();

            logger.info("Working for: " + mention.getUser().getScreenName() + ", tweet id: " + requestorTweetId + " target tweet id: " + targetTweetId);

            if(targetTweetId == -1 || targetTweetId == null) {
               logger.info("Tweet Id: " + requestorTweetId + " empty mention");
               continue;
            }

            if (mentionService.isRequestAlreadyFulfilled(String.valueOf(requestorTweetId), String.valueOf(targetTweetId))) {
                logger.info("r_id: " + targetTweetId + ", " + requestorTweetId + " ALREADY FULFILLED");
                continue;
            }

            try {
                Status tweetToExtractUrlFrom = twitter.showStatus(targetTweetId);

                URLEntity[] urls = tweetToExtractUrlFrom.getURLEntities();
                if (urls.length == 0) {
                    logger.info("nothing to take screenshots of");
                    break;
                }

                String targetUrl = "";
                for (URLEntity url : urls) {
                    logger.info("url: " + url.getURL());
                    targetUrl = url.getURL();
                    break;

                }
                if (targetUrl.isEmpty()) {
                    break;
                }
                driver.shoot(targetUrl);

                if(driver.getFileMap().size() == 0) {
                    logger.info("No tweet uploaded. Only one screenshot");
                    continue;
                }

                String customMessage = "Reply to tweets with external urls and I will get back with screenshots!. Thanks. #SavedYouAClick ";
                Status s1 = twitter.showStatus(targetTweetId);
                String fullStatusMessage = customMessage + " @" + mention.getUser().getScreenName() + " ";
                StatusUpdate statusUpdate = new StatusUpdate(fullStatusMessage);

                long[] mediaIds = new long[driver.getFileMap().size()];
                logger.info("Size of filemap: " + driver.getFileMap().size());
                long[] finalMediaIds = mediaIds;
                driver.getFileMap().forEach((file, counter) -> {
                    int innerCounter = (Integer) counter;
                    try {
                        UploadedMedia media = twitter.uploadMedia((File) file);
                        finalMediaIds[--innerCounter] = media.getMediaId();
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                });
                statusUpdate.setMediaIds(finalMediaIds);
                driver.emptyMap();
                Status reply = twitter.updateStatus(statusUpdate.inReplyToStatusId(mention.getId()));
                Mention tweetToStore = new Mention();
                tweetToStore.setRequestorTweetId(String.valueOf(requestorTweetId));
                tweetToStore.setTargetTweetId(String.valueOf(targetTweetId));
                tweetToStore.setFullText(tweetToExtractUrlFrom.getText());
                tweetToStore.setTargetUrl(targetUrl);
                tweetToStore.setRequestorScreenName(mention.getUser().getScreenName());
                tweetToStore.setTargetScreenName(s1.getUser().getScreenName());
                Date now = new Date();
                tweetToStore.setCreatedAt(now);
                mentionService.save(tweetToStore);
                driver.close();
                Thread.sleep(1000);
            } catch (TwitterException ex) {
                logger.info(ex.getMessage() + ". Target Tweet Id: " + targetTweetId );
            }
        }
        return "ok";
    }
}