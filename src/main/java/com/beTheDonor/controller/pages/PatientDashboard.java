package com.beTheDonor.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PatientDashboard {
    @RequestMapping("/patient/dashboard")
    public ModelAndView patientDashboard() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("patientDashboard.html");
        return modelAndView;
    }
}
