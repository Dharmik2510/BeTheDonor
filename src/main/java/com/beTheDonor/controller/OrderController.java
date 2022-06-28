package com.beTheDonor.controller;


import com.beTheDonor.repository.OrderItemsRepository;
import com.beTheDonor.repository.OrderRepository;
import com.beTheDonor.repository.UserRepository;
import com.beTheDonor.entity.PatientOrdersResponse;
import com.beTheDonor.exception.ErrorResponse;
import com.beTheDonor.service.CreditAmountService;
import com.beTheDonor.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
public class OrderController
{
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;

    @Autowired
    CreditAmountService creditAmountService;

    ErrorResponse er = new ErrorResponse();

    int successCode = 200;
    int failCode= 500;
    @PostMapping(value = "/api/v1/patient/order")
    public ErrorResponse addOrder(@RequestBody JSONObject payload, HttpServletRequest request) throws Exception {
        Principal principal = request.getUserPrincipal();
        String userId = principal.getName();
        Boolean response = orderService.addOrder(payload,userId);
        if(response == true) {
            creditAmountService.orderFromCredits();
            er.setCode(successCode);
            er.setMessage("Order Added Successfully.");
        }
        else {
            er.setCode(failCode);
            er.setMessage("Failed to add order. Try after sometime.");
        }
        return er;
    }

    @GetMapping(value = "/api/v1/patient/getOrders")
    @ResponseBody
    public List<PatientOrdersResponse> getOrdersByUserId(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String userId = principal.getName();
        List<PatientOrdersResponse> orderResponses = orderService.getOrdersResponseByUserId(userId);
        return orderResponses;
    }

    @GetMapping(value = "/api/v1/donor/getOrders")
    public List<PatientOrdersResponse> getAllOrders() {
        List<PatientOrdersResponse> orderResponses = orderService.getOrderResponse();
        return orderResponses;
    }
}