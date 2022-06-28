package com.beTheDonor.controller;

import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.Donors;
import com.beTheDonor.repository.OrderRepository;
import com.beTheDonor.repository.UserRepository;
import com.beTheDonor.service.ApplicationUserService;
import com.beTheDonor.service.DonorService;
import com.beTheDonor.service.impl.RiderService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class DonorControllerTest {

    @Autowired
    DonorController donorController;
    @MockBean
    DonorService donorService;
    @MockBean
    Model model;
    @MockBean
    ApplicationUserService applicationUserService;
    @MockBean
    BCryptPasswordEncoder passwordEncoder;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    HttpServletRequest request;
    @MockBean
    Principal principal;

    @MockBean
    ApplicationUser applicationUser;

    @MockBean
    Donors donor;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        donorController = Mockito.spy(donorController);
        mockMvc = MockMvcBuilders.standaloneSetup(donorController).build();
    }

    @Test
    @Order(1)
    @DisplayName("testing order Status Change to pending delivery after Checkout")
    void checkStatusChangeIsTrue() throws Exception {
        JSONObject jsonObject = new JSONObject();
        Mockito.doReturn(true).when(donorService).changeStatusOfOrder(jsonObject);
        Assertions.assertTrue(donorController.changeStatusAfterOrder(jsonObject));

    }

    @Test
    @Order(2)
    @DisplayName("Testing storing total amount")
    void checkStoreTotalAmountTrue() throws Exception {
        JSONObject jsonObject = new JSONObject();

        String userId = "prachiraval2608@gmail.com";
        Mockito.doReturn(principal).when(request).getUserPrincipal();
        Mockito.doReturn(applicationUser).when(userRepository).getByEmail(userId);
        Mockito.doReturn(1L).when(applicationUser).getId();

        Long id = 1L;
        Mockito.doReturn(true).when(donorService).storeTotalAmount(jsonObject, id);
        mockMvc.perform(post("/donationInfo")
                        .principal(principal)
                        .param("email","dhsoni2510@gmail.com")
                        .param("password","123456"))
                .andDo(print());

    }

    @Test
    @Order(3)
    @DisplayName("Check donation amount by id")
    void checkgetDonationById() throws Exception {
        JSONObject jsonObject = new JSONObject();
        String userId = "prachiraval2608@gmail.com";
        Mockito.doReturn(principal).when(request).getUserPrincipal();
        Mockito.doReturn(userId).when(principal).getName();
        Long id = 1L;
        mockMvc.perform(get("/getDonationById")
                .param("email", userId)
                .param("password", "123456"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", donor);
        Mockito.doReturn(donor).when(donorService).getDonationById(id);


    }

    @Test
    @Order(4)
    @DisplayName("Redirect to html page")
    public void redirectToDonorView() throws Exception {
        mockMvc.perform(get("/donorview", model))
                .andDo(print())
                .andExpect(view().name("donorView"));

    }
}