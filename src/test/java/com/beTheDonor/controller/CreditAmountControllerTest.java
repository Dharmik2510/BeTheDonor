package com.beTheDonor.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = CreditAmountController.class)
@AutoConfigureMockMvc
class CreditAmountControllerTest {

    private MockMvc mockMvc;

    @Autowired
    CreditAmountController creditAmountController;

    @MockBean
    CreditAmountService creditAmountService;

    @MockBean
    ApplicationUserService applicationUserService;

    @MockBean
    CreditAmountRepository creditAmountRepository;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    CreditAmount creditAmount;



    @BeforeEach
    void setUp() {
        creditAmountController = Mockito.spy(creditAmountController);
        mockMvc = MockMvcBuilders.standaloneSetup(creditAmountController).build();
    }

    @Test
    void updateTip() throws Exception {
        Double tipPercent = 5.0;
        Mockito.doNothing().when(creditAmountService).updateTipPercent(tipPercent);
        mockMvc.perform(get("/api/v1/autoPayByCredit/updateTipPercent/{tipPercent}",tipPercent));
    }

    @Test
    void getTip() throws Exception {
        Double tip = 5.0;
        Mockito.doReturn(creditAmount).when(creditAmountRepository).getById(1);
        Mockito.doReturn(tip).when(creditAmount).getRiderTipPercent();
        mockMvc.perform(get("/api/v1/autoPayByCredit/getTipPercent")).andExpect(status().isOk()).andReturn();
        Assertions.assertEquals(tip,creditAmountController.getTip());
    }

    @Test
    void updateCreditAmount() throws Exception {
        Double amount = 100.0;
        Mockito.doNothing().when(creditAmountService).updateCreditAmount(amount);
        mockMvc.perform(get("/api/v1/autoPayByCredit/updateCreditAmount/{amount}",amount)).andExpect(status().isOk()).andReturn();
    }
}
