package com.example.springboot.Service;

import com.example.springboot.Entity.Mention;
import com.example.springboot.Repository.MentionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MentionService {
    @Autowired
    private MentionRepository mentionRepository;

    public Boolean isRequestAlreadyFulfilled(String requestorTweetId, String targetTweetId) {
        if(mentionRepository.existsByRequestorTweetIdAndTargetTweetId(requestorTweetId, targetTweetId)) {
            return true;
        }
        return false;
    }

    public void save(Mention mention) {
         mentionRepository.save(mention);
    }
}
