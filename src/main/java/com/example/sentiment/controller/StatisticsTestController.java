package com.example.sentiment.controller;

import com.example.sentiment.utilities.Statistics;
import com.example.sentiment.utilities.StatisticsTestObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StatisticsTestController {

    @GetMapping("/stat")
    public ModelAndView getAverage() {

        // Like an animal, only for testing
        List<StatisticsTestObject> objects = new ArrayList<>();
        String keyword = "academy";
        objects.add(new StatisticsTestObject(1, "tweettext", 0.56879, "academy"));
        objects.add(new StatisticsTestObject(2, "tweettext", 0.89798, "academy"));
        objects.add(new StatisticsTestObject(3, "tweettext", 0.99999, "academy"));
        objects.add(new StatisticsTestObject(4, "tweettext", 0.86589, "academy"));
        objects.add(new StatisticsTestObject(5, "tweettext", 0.48797, "academy"));

        double average = Statistics.getAverageByKeyWord(keyword, objects);

        return new ModelAndView("stat")
                .addObject("keyword", keyword)
                .addObject("averageScore", average);
    }
}
