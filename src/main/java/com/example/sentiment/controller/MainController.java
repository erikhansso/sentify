package com.example.sentiment.controller;

import com.example.sentiment.apis.TwitterCommunication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class MainController {


    @GetMapping("/")
    public ModelAndView getIndex() throws IOException {

        TwitterCommunication twitterCommunication = new TwitterCommunication();

        String testTweet = twitterCommunication.getTweets();
        return new ModelAndView("demo")
                .addObject("tweet", testTweet);
    }
}
