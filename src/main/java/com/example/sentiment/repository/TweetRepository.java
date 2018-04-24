package com.example.sentiment.repository;

import com.example.sentiment.entities.Query;
import com.example.sentiment.entities.Tweet;
import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<Tweet, Long> {

    //Not sure if this works, testing is possible when we have a list of tweet objects in maincontroller
    Iterable<Tweet> findByQuery(Query query);

}
