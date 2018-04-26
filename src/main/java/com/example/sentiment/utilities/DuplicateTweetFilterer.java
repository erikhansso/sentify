package com.example.sentiment.utilities;

import com.example.sentiment.entities.Tweet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateTweetFilterer {

    public static List<Tweet> duplicateTweetFilterer(List<Tweet> tweetsToFilter) {

        Map<String, Long> filterMap = new HashMap<>();
        List<Tweet> filteredTweets = new ArrayList<>();

        for (Tweet unfilteredTweet : tweetsToFilter) {
            filterMap.put(unfilteredTweet.getTweetText(), unfilteredTweet.gettweetId());
        }

        for (Tweet unfilteredTweet : tweetsToFilter) {
            if(filterMap.containsValue(unfilteredTweet.gettweetId())){
                filteredTweets.add(unfilteredTweet);
            }
        }

        return filteredTweets;
    }

}
