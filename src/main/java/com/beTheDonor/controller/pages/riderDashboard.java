package com.beTheDonor.controller.pages;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class riderDashboard
{
    @RequestMapping("/riderDashboard")
    public ModelAndView getRiderPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("riderDashboard.html");
        return modelAndView;
    }
}
