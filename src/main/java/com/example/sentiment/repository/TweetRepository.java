package com.example.sentiment.repository;

import com.example.sentiment.entities.QueryEntity;
import com.example.sentiment.entities.Tweet;
import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<Tweet, Long> {

    Iterable<Tweet> findByQuery(QueryEntity queryEntity);

}
