package com.beTheDonor.controller;

import com.beTheDonor.controller.requestbody.ProductRequest;
import com.beTheDonor.controller.requestbody.RegistrationRequest;
import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.Product;
import com.beTheDonor.service.AdminService;
import com.beTheDonor.service.ProductService;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class AdminController {
    ProductService productService;
    AdminService adminService;
    @GetMapping("/api/v1/admin")
    public String adminDashboard(Model model)
    {
        List<Product> products = productService.getProducts();
        model.addAttribute("products",products);

        List<ApplicationUser> users = adminService.getUsers();
        model.addAttribute("users", users);

        List<ApplicationUser> patients = adminService.getPatients();
        model.addAttribute("patients", patients);

        List<ApplicationUser> riders = adminService.getRiders();
        model.addAttribute("riders", riders);

        List<ApplicationUser> donors = adminService.getDonors();
        model.addAttribute("donors", donors);

        model.addAttribute("product",new ProductRequest());
        return "admindashboard";
    }

    @GetMapping("/api/v1/admin/addProductsInTable")
    public String addProducts(@ModelAttribute ProductRequest productRequest)
    {
        adminService.add(productRequest);
        return "redirect:/api/v1/admin";
    }

    @GetMapping("/api/v1/admin/deleteProduct/{id}")
    @ResponseBody
    public void delete(@PathVariable long id)
    {
        System.out.println(id);
        adminService.delete(id);
    }

    @GetMapping("/api/v1/admin/updateProductsInTable/{qty}/{price}/{id}")
    public String updateProducts(@PathVariable int qty, @PathVariable double price, @PathVariable long id )
    {
        adminService.update(qty, price, id);
        return "redirect:/admindashboard";
    }
}
