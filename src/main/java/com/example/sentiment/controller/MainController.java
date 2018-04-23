package com.example.sentiment.controller;

import com.example.sentiment.apis.TwitterCommunication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class MainController {

    @Autowired
    TwitterCommunication twitterCommunication;

    @GetMapping("/")
    public ModelAndView getIndex() throws IOException {

        String testTweet = twitterCommunication.getTweets();
        return new ModelAndView("demo")
                .addObject("tweet", testTweet);
    }
}
