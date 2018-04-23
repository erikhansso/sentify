package com.example.sentiment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/")
    public ModelAndView getIndex(){
//        String test = System.getenv("E_testKey");

        return new ModelAndView("demo").addObject("test", "testar");
    }
}
