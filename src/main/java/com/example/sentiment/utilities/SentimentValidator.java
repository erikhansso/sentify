package com.example.sentiment.utilities;

import com.example.sentiment.entities.Tweet;

import java.util.List;

public class SentimentValidator {

    public static List<Tweet> sentimentValidator(List<Tweet> uniqueTweetsNotInDb, List<Tweet> newTweetsWithValidSentiment) {
        for (Tweet tweetObject : uniqueTweetsNotInDb) {
            if (tweetObject.getSentimentScore() != 0.5 && tweetObject.getSentimentScore() != 0.0) {
                newTweetsWithValidSentiment.add(tweetObject);
            }
        }
        return newTweetsWithValidSentiment;
    }

}
