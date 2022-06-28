package com.beTheDonor.controller;

import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.Donors;
import com.beTheDonor.exception.ErrorResponse;
import com.beTheDonor.repository.*;
import com.beTheDonor.config.PasswordEncoder;
import com.beTheDonor.service.ApplicationUserService;
import com.beTheDonor.service.DonorService;
import com.beTheDonor.service.ProductService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DonorController {
    @Autowired
    ApplicationUserService applicationUserService;
    @Autowired
    DonorService donorService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderItemsRepository orderItemsRepository;
    @Autowired
    DonorRepository donorRepository;
    @Autowired
    private DaoAuthenticationProvider authenticationManager;
    @Autowired
    private ApplicationUserService userDetailsService;

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @GetMapping("/donorview")
    public String donorLogin(Model model) {
        return "donorView";
    }


    @PostMapping(value = "/finalOrderDonor")
    public Boolean changeStatusAfterOrder(@RequestBody JSONObject payload) throws Exception {
        return donorService.changeStatusOfOrder(payload);
    }

    @PostMapping(value = "/donationInfo")
    public Boolean storeTotalAmount(@RequestBody JSONObject payload, HttpServletRequest request) throws Exception {
        Principal principal = request.getUserPrincipal();
        Donors donor;
        String userId = principal.getName();
        ApplicationUser user = userRepository.getByEmail(userId);
        Long id = user.getId();
        Boolean response = donorService.storeTotalAmount(payload, id);
        return response;
    }

    @GetMapping(value = "/getDonationById")
    @ResponseBody
    public Map<String, Object> getDonationById(HttpServletRequest request) throws Exception {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            Principal principal = request.getUserPrincipal();
            String userId = principal.getName();
            Long id = userRepository.getByEmail(userId).getId();
            Donors donor = donorService.getDonationById(id);
            responseMap.put("data", donor);
        } catch (Exception ex) {
            responseMap.put("error", ex.getMessage());
        }
        return responseMap;
    }

}
