package com.example.springboot.Controller;

import com.example.springboot.Entity.Mentions;
import com.example.springboot.Repository.MentionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import twitter4j.conf.ConfigurationBuilder;

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
	public String tweet() {
		return "I will tweet here";
	}
}