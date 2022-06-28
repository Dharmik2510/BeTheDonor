package com.beTheDonor.controller.pages;

import com.beTheDonor.entity.CreditAmount;
import com.beTheDonor.repository.CreditAmountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class DonateByAmount {

    @Autowired
    CreditAmountRepository creditAmountRepository;

    @RequestMapping("/donate")
    public ModelAndView donateByAmount(Model model) {
        CreditAmount creditAmount = creditAmountRepository.getById(1);
        model.addAttribute("fundRaised", creditAmount.getFundRaised());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("donate.html");
        return modelAndView;
    }

    @RequestMapping("/thank-you")
    public ModelAndView thankYou() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("thank-you.html");
        return modelAndView;
    }
}

