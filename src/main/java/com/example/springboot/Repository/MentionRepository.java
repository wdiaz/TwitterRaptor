package com.example.springboot.Repository;

import com.example.springboot.Entity.Mention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentionRepository extends JpaRepository<Mention, Integer> {
    boolean existsByRequestorTweetIdAndTargetTweetId(String requestorTweetId, String targetTweetId);
}
