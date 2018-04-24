package com.example.sentiment.entities;

import java.util.List;

public class SentimentQueryBuilder {

    public static Documents buildSentimentQueries(List<String> tweetTexts){

        Documents docs = new Documents();
        int i = 1;

        for (String tweetText : tweetTexts) {
            docs.add(Integer.toString(i), "sv", tweetText);
            i++;
        }


        return docs;

    }

}
