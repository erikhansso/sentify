package com.example.sentiment.controller;

import com.example.sentiment.apis.TwitterCommunication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @Autowired
    TwitterCommunication twitterCommunication;

    @GetMapping("/demo")
    public ModelAndView getDemo() {
        String testTweet = twitterCommunication.getTweets();
        return new ModelAndView("demo")
                .addObject("tweet", testTweet);
    }

    @GetMapping("/")
    public ModelAndView getStartPage(){
        return new ModelAndView("index");
    }

    @PostMapping("/searchForTweets")
    @ResponseBody
    public String getTweets(@RequestParam String searchInput){
        return twitterCommunication.getTweetByQuery(searchInput);
    }
}
