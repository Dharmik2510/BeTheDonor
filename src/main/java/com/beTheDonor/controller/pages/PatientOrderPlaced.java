package com.beTheDonor.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PatientOrderPlaced {
    @RequestMapping("/patient/order-placed")
    public ModelAndView patientOrders() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("patientOrderSuccessPage.html");
        return modelAndView;
    }
}
