package com.beTheDonor.controller;

import com.beTheDonor.constant.Constants;
import com.beTheDonor.entity.PatientOrdersResponse;
import com.beTheDonor.entity.Response;
import com.beTheDonor.service.AnalyticsService;
import com.beTheDonor.service.StripeService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PaymentController {

    @Value("${STRIPE_PUBLIC_KEY}")
    private String API_PUBLIC_KEY;

    @Autowired
    ObjectFactory<HttpSession> httpSessionFactory;

    @Autowired
    AnalyticsService analyticsService;

    private final StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/api/v1/create-charge")
    public @ResponseBody Response createCharge(String email, String token, String amount) {
        if (token == null) {
            return new Response(false, "Stripe payment token is missing. please try again later.");
        }
        String chargeId = stripeService.createCharge(email, token, Double.parseDouble(amount)* Constants.multiply_value);// 9.99 usd
        HttpSession session = httpSessionFactory.getObject();
        if (chargeId == null) {
            session.setAttribute("payment_status","false");
            session.setAttribute("payment_chargeId","0");
            return new Response(false, "An error occurred while trying to charge.");
        }

        session.setAttribute("payment_status","true");
        session.setAttribute("payment_chargeId",chargeId);
        return new Response(true, chargeId);
    }

    @GetMapping("/paymentSuccess")
    public String paymentSuccess(Model model) {
        HttpSession session = httpSessionFactory.getObject();
        boolean payment_status = session.getAttribute("payment_status").toString().equals("true");
        String payment_transactionId = session.getAttribute("payment_chargeId").toString();
        model.addAttribute("status", payment_status);
        model.addAttribute("statusText", payment_status?"Success":"Failed");
        model.addAttribute("chargeId", payment_transactionId);
        session.removeAttribute("payment_status");
        session.removeAttribute("payment_chargeId");
        return "transactionSuccess";
    }

    // storing amount from donorview to payment gateway
//    payload - amount in json format
    @PostMapping(value = "/paymentAmount")
    public @ResponseBody String paymentAmountAdd(@RequestBody JSONObject payload) {
        HttpSession session = httpSessionFactory.getObject();
        session.setAttribute("payment_amountToPay",payload.get("amount").toString());
        System.out.println("===Payment Amount : "+session.getAttribute("payment_amountToPay"));
        return "null";
    }

    @GetMapping("/paymentGateway")
    public String paymentGateway(Model model) {
        HttpSession session = httpSessionFactory.getObject();
        String amount = session.getAttribute("payment_amountToPay").toString();
        session.removeAttribute("payment_amountToPay");
        System.out.println("===Payment GATEWAY : "+amount);
        model.addAttribute("amount",amount);
        model.addAttribute("stripePublicKey", API_PUBLIC_KEY);
        return "paymentGateway";
    }
}