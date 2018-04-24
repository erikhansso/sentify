package com.example.sentiment.entities;

import com.example.sentiment.pojos.Documents;

import java.util.List;

public class SentimentQueryBuilder {

    public static Documents buildSentimentQueries(List<Tweet> tweetObjects){

        Documents docs = new Documents();

        for (Tweet tweetObject : tweetObjects) {
            docs.add(String.valueOf(tweetObject.getAzureId()), tweetObject.getLanguage(), tweetObject.getTweetText());
        }

        return docs;

    }

}
