package com.example.sentiment.pojos;

import com.example.sentiment.entities.Tweet;
import com.example.sentiment.pojos.Documents;

import java.util.List;

public class SentimentQueryBuilder {

    public static Documents buildSentimentQueries(List<Tweet> tweetObjects){

        Documents docs = new Documents();

        for (Tweet tweetObject : tweetObjects) {
            docs.add(String.valueOf(tweetObject.gettweetId()), tweetObject.getLanguage(), tweetObject.getTweetText());
        }

        return docs;

    }

}
