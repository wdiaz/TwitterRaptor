package com.example.springboot.Controller;

import com.example.springboot.Entity.Mentions;
import com.example.springboot.Repository.MentionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import twitter4j.*;

import java.util.List;

@RestController
public class HelloController {
	@Autowired
	private MentionsRepository mentionsRepository;
	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping(path="/all")
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
		List<Status> statuses = twitter.getMentionsTimeline();
		return statuses.toString();
	}
}