package com.example.springboot.Controller;

import com.example.springboot.Application;
import com.example.springboot.Entity.Mention;
import com.example.springboot.Repository.MentionRepository;
import com.example.springboot.Service.Driver.DriverFactory;
import com.example.springboot.Service.Driver.Types.IType;
import com.example.springboot.Service.MentionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class HelloController {
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
    public String mentions() throws TwitterException, IOException {
        Twitter twitter = TwitterFactory.getSingleton();


        int page = 1;
        int totalTweets = page * 10;
        Paging paging = new Paging(1, totalTweets);
        List<Status> mentions = twitter.getMentionsTimeline(paging);


        int counter = 0;
        for (Status mention : mentions) {
            String inReplyToStatusId = String.valueOf(mention.getInReplyToStatusId());

            Long requestorTweetId = mention.getId();
            String tweetText = mention.getText();
            String regex = "\\bscreenshot\\b";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(tweetText);
            boolean screenshotRequest = matcher.find();
            if (screenshotRequest) {

                if(mentionService.isRequestAlreadyFulfilled(String.valueOf(requestorTweetId), inReplyToStatusId)) {
                    logger.info("r_id: " + inReplyToStatusId + ", " + requestorTweetId + " ALREADY FULFILLED");
                    continue;
                }

                logger.info("tweet id to extract url from: " + inReplyToStatusId);
                logger.info("requestor tweet id: " + requestorTweetId);
                Status tweetToExtractUrlFrom = twitter.showStatus(mention.getInReplyToStatusId());


                logger.info("Twitter here: " + tweetToExtractUrlFrom.getText());
                URLEntity[] urls = tweetToExtractUrlFrom.getURLEntities();
                String txt = tweetToExtractUrlFrom.getText();

                String targetUrl = "";
                for (URLEntity url : urls) {
                    logger.info("url: " + url.getURL());
                    targetUrl = url.getURL();
                    break;

                }
                if (targetUrl.isEmpty() ) {
                    break;
                }

               IType chromeDriver = DriverFactory.create("chrome");
                logger.info("The target url is: " + targetUrl);
                chromeDriver.shoot(targetUrl);

                Long requestFromTweetId = mention.getId();
                logger.info("full tweet from :" + mention.toString());
                logger.info("Miguel Tweet ID" + requestFromTweetId);

                String customMessage = "Here you have it. Thanks ";
                Status s1 = twitter.showStatus(Long.parseLong(inReplyToStatusId));

                logger.info("Target full tweet: " + s1.getId());
                String fullStatusMessage = customMessage + " @" + mention.getUser().getScreenName() + " ";

                File imagefile1 = new File("/tmp/test-image1.jpg");
                File imagefile2 = new File("/tmp/test-image2.jpg");
                File imagefile3 = new File("/tmp/test-image3.jpg");
                File imagefile4 = new File("/tmp/test-image4.jpg");

                long[] mediaIds = new long[4];
                UploadedMedia media1 = twitter.uploadMedia(imagefile1);
                mediaIds[0] = media1.getMediaId();

                UploadedMedia media2 = twitter.uploadMedia(imagefile2);
                mediaIds[1] = media2.getMediaId();

                UploadedMedia media3 = twitter.uploadMedia(imagefile3);
                mediaIds[2] = media3.getMediaId();

                UploadedMedia media4 = twitter.uploadMedia(imagefile4);
                mediaIds[3] = media4.getMediaId();
                StatusUpdate statusUpdate = new StatusUpdate(fullStatusMessage);
                statusUpdate.setMediaIds(mediaIds);

                Status reply = twitter.updateStatus(statusUpdate.inReplyToStatusId(mention.getId()));

                Mention tweetToStore = new Mention();
                logger.info("Full text: " + tweetToExtractUrlFrom.getText());
                tweetToStore.setRequestorTweetId(String.valueOf(requestorTweetId));
                tweetToStore.setTargetTweetId(inReplyToStatusId);
                tweetToStore.setFullText(tweetToExtractUrlFrom.getText());
                tweetToStore.setTargetUrl(targetUrl);
                tweetToStore.setRequestorScreenName(mention.getUser().getScreenName());
                tweetToStore.setTargetScreenName(s1.getUser().getScreenName());
                Date now = new Date();
                tweetToStore.setCreatedAt(now);
                mentionService.save(tweetToStore);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                      logger.info("Exception when waiting after mentions save");
                }
            }
        }
        return "ok";
    }
}