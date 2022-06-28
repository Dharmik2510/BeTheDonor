package com.beTheDonor.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PatientOrders {
    @RequestMapping("/patient/orders")
    public ModelAndView patientOrders() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("patientOrders.html");
        return modelAndView;
    }
}
