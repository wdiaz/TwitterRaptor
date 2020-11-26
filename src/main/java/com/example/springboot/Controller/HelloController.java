package com.example.springboot.Controller;

import com.example.springboot.Application;
import com.example.springboot.Entity.Mentions;
import com.example.springboot.Repository.MentionsRepository;
import com.example.springboot.Service.Driver.DriverFactory;
import com.example.springboot.Service.Driver.Types.IType;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public String mentions() throws TwitterException, IOException {
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


            //logger.info("miguel: " + tweetText);


            String regex = "\\bmira\\b";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(tweetText);
            boolean screenshotRequest = matcher.find();
            if (screenshotRequest) {
                logger.info("Screenshot Requested ");
                logger.info("In reply to: " + status.getInReplyToStatusId());
                Status inReplytoStatus = twitter.showStatus(status.getInReplyToStatusId());
                logger.info("Twitter here" + inReplytoStatus.getText());
                URLEntity[] urls = inReplytoStatus.getURLEntities();


                String targetUrl = "";
                for (URLEntity url : urls) {
                    targetUrl = url.getURL();
                    break;
                }
                IType chromeDriver = DriverFactory.create("chrome");
                logger.info("The target url is: " + targetUrl);
                chromeDriver.shoot(targetUrl);

                Long requestFromTweetId = status.getId();
                logger.info("Miguel Tweet ID" + requestFromTweetId);

                String customMessage = "Aquí tienes tus capturas. Dile no al Click & Bait!. Gracias.";
                Status s1 = twitter.showStatus(Long.parseLong(inReplyToStatusId));
                String fullStatusMessage = customMessage + " @" + status.getUser().getScreenName() + " ";
                //StatusUpdate statusUpdate = new StatusUpdate(" @" + status.getUser().getScreenName() + " " + customMessage);

                long[] mediasId = new long[4];

                String[] mediaFiles = {
                        "/tmp/test-image1.jpg",
                        "/tmp/test-image2.jpg",
                        "/tmp/test-image3.jpg",
                        "/tmp/test-image4.jpg"
                };
                /*int mediaCounter = 0;
                for (String fileName : mediaFiles) {
                    File mediaFile = new File(fileName);
                    //statusUpdate.setMedia(mediaFile);
                    File mediaFile = new File(fileName);
                    UploadedMedia uploadedMedia = twitter.uploadMedia(mediaFile);
                    mediasId[mediaCounter] = uploadedMedia.getMediaId();
                    counter = counter + 1;
                }
                statusUpdate.setMediaIds(mediasId);
                 */
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

                Status reply = twitter.updateStatus(statusUpdate.inReplyToStatusId(status.getId()));

            }

            /*logger.info("In reply to: " + status.getInReplyToStatusId());
            Status inReplytoStatus = twitter.showStatus(status.getInReplyToStatusId());
            logger.info("Twitter here" + inReplytoStatus.getText());
            break;*/
            /*URLEntity[] urls = status.getURLEntities();
            for (URLEntity url : urls) {
                logger.info("URL: " + url.getURL());
            }
            logger.info(++counter + " -> " + inReplyToStatusId + " = " + inReplyToScreenNAme + "=" + tweetText);*/
        }
        //return statuses.toString();
        return "ok";
    }
}