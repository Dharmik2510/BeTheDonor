package com.beTheDonor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class HomePageController {

    @GetMapping(value = {"/","/homePage"})
    public String HomePage(Model model) {
        return "HomePage";
    }
}
