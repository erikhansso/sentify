package com.example.sentiment.entities;

import com.example.sentiment.pojos.Documents;

import java.util.List;

public class SentimentQueryBuilder {

    public static Documents buildSentimentQueries(List<TestTweet> tweetObjects){

        Documents docs = new Documents();

        for (TestTweet tweetObject : tweetObjects) {
            docs.add(String.valueOf(tweetObject.getAzureId()), tweetObject.getLanguage(), tweetObject.getTweetText());
        }

        return docs;

    }

}
