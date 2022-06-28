package com.beTheDonor.controller;

import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.PatientOrdersResponse;
import com.beTheDonor.exception.ErrorResponse;
import com.beTheDonor.repository.OrderItemsRepository;
import com.beTheDonor.repository.OrderRepository;
import com.beTheDonor.repository.UserRepository;
import com.beTheDonor.service.ApplicationUserService;
import com.beTheDonor.service.CreditAmountService;
import com.beTheDonor.service.OrderService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc
class OrderControllerTest {

    private MockMvc mockMvc;

    @Autowired
    OrderController orderController;

    @MockBean
    ApplicationUserService applicationUserService;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    HttpServletRequest request;

    @MockBean
    ErrorResponse errorResponse;

    @MockBean
    Principal principal;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    OrderService orderService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    OrderItemsRepository orderItemsRepository;

    @MockBean
    CreditAmountService creditAmountService;

    @BeforeEach
    void setUp() {
        orderController = Mockito.spy(orderController);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void addOrder() throws Exception {
        mockMvc.perform(post("/api/v1/patient/order")
                .principal(principal)
                .param("payload","'result':[{'productId':'3','quantity':'1'},{'productId':'1','quantity':'1'}],'total':'300.00','address':[{'address':'1991 Brunswick st','city':'Halifax','province':'Nova scotia','country':'Canada','postalCode':'B3J2G9'}]")
                .contentType(MediaType.APPLICATION_JSON).content("").accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void getOrdersByUserId() throws Exception {
        Mockito.doReturn(principal).when(request).getUserPrincipal();
        Mockito.doReturn("test@gmail.com").when(principal).getName();
        List<PatientOrdersResponse> orderResponses = new ArrayList<>();
        Mockito.doReturn(orderResponses).when(orderService).getOrdersByUserId("test@gmail.com");
        mockMvc.perform(get("/api/v1/patient/getOrders").param("request","request")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON).content("").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertEquals(orderResponses,orderController.getOrdersByUserId(request));
    }

    @Test
    void getAllOrders() throws Exception {
        List<PatientOrdersResponse> orderResponses = new ArrayList<>();
        Mockito.doReturn(orderResponses).when(orderService).getOrderResponse();
        mockMvc.perform(get("/api/v1/donor/getOrders")
                        .contentType(MediaType.APPLICATION_JSON).content("").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Assertions.assertEquals(orderResponses,orderController.getAllOrders());
    }
}