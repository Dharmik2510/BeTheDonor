package com.beTheDonor.controller;

import com.beTheDonor.controller.requestbody.ProductRequest;
import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.Product;
import com.beTheDonor.repository.OrderRepository;
import com.beTheDonor.service.AdminService;
import com.beTheDonor.service.ApplicationUserService;
import com.beTheDonor.service.ProductService;
import com.beTheDonor.service.impl.RiderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = AdminController.class)
@AutoConfigureMockMvc
class AdminControllerTest{

    private MockMvc mockMvc;

    @Autowired
    AdminController adminController;

    @MockBean
    ProductService productService;

    @MockBean
    AdminService adminService;

    @MockBean
    ProductRequest productRequest;

    @MockBean
    Model model;

    @MockBean
    ApplicationUserService applicationUserService;

    @MockBean
    BCryptPasswordEncoder passwordEncoder;

    @MockBean
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        adminController = Mockito.spy(adminController);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void addProducts() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("DummyName");
        productRequest.setPrice(20.0);
        productRequest.setQuantity(50);
        productRequest.setComment("kg");
        productRequest.setCategory("snacks");

        mockMvc.perform(get("/api/v1/admin/addProductsInTable",productRequest));

        Mockito.doNothing().when(adminService).add(productRequest);
    }

    @Test
    void delete() throws Exception{
        long id = 1;

        mockMvc.perform(get("/api/v1/admin/deleteProduct/{id}",id));

        Mockito.doNothing().when(adminService).delete(id);
    }

    @Test
    void updateProducts() throws Exception{
        int qty = 20;
        double price = 50.0;
        long id = 1;

        mockMvc.perform(get("/api/v1/admin/updateProductsInTable/{qty}/{price}/{id}",qty,price,id));

        Mockito.doNothing().when(adminService).update(qty,price,id);
    }

    @Test
    void adminDashboard() throws Exception{

        List<Product> products = new ArrayList<>();
        Mockito.when(productService.getProducts()).thenReturn(products);

        List<ApplicationUser> users = new ArrayList<>();
        Mockito.when(adminService.getUsers()).thenReturn(users);

        List<ApplicationUser> patients = new ArrayList<>();
        Mockito.when(adminService.getPatients()).thenReturn(patients);

        List<ApplicationUser> donors = new ArrayList<>();
        Mockito.when(adminService.getDonors()).thenReturn(donors);

        List<ApplicationUser> riders = new ArrayList<>();
        Mockito.when(adminService.getRiders()).thenReturn(riders);

        mockMvc.perform(get("/api/v1/admin",model).param("users","users")
                        .param("patients","patients")
                        .param("donors","donors")
                        .param("riders","riders"))
                .andDo(print())
                .andExpect(view().name("admindashboard"));

    }
}
