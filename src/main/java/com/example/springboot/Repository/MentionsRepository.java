package com.example.springboot.Repository;

import com.example.springboot.Entity.Mentions;
import org.springframework.data.repository.CrudRepository;

public interface MentionsRepository extends CrudRepository<Mentions, Integer> {
}
