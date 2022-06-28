package com.beTheDonor.controller.pages;

import com.beTheDonor.controller.CreditAmountController;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;


import com.beTheDonor.entity.CreditAmount;
import com.beTheDonor.repository.CreditAmountRepository;
import com.beTheDonor.service.ApplicationUserService;
import com.beTheDonor.service.CreditAmountService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DonateByAmount.class)
@AutoConfigureMockMvc
class DonateByAmountTest {
    private MockMvc mockMvc;

    @Autowired
    DonateByAmount donateByAmount;

    @MockBean
    ApplicationUserService applicationUserService;

    @MockBean
    CreditAmountRepository creditAmountRepository;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    Model model;

    @MockBean
    CreditAmount creditAmount;


    @BeforeEach
    void setUp() {
        donateByAmount = Mockito.spy(donateByAmount);
        mockMvc = MockMvcBuilders.standaloneSetup(donateByAmount).build();
    }
    @Test
    void donateByAmount() throws Exception {
        Mockito.doReturn(creditAmount).when(creditAmountRepository).getById(1);
        Mockito.doReturn(10.0).when(creditAmount).getFundRaised();
        Mockito.doReturn(model).when(model).addAttribute("fundRaised", 10.0);

        mockMvc.perform(get("/donate",model))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void thankYou() throws Exception {
        mockMvc.perform(get("/thank-you",model))
                .andExpect(status().isOk())
                .andReturn();
    }
}