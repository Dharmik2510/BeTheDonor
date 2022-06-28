package com.beTheDonor.controller.pages;

import com.beTheDonor.service.ApplicationUserService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PatientOrderPlaced.class)
@AutoConfigureMockMvc
class PatientOrderPlacedTest {

    private MockMvc mockMvc;

    @Autowired
    PatientOrderPlaced patientOrderPlaced;

    @MockBean
    ApplicationUserService applicationUserService;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        patientOrderPlaced = Mockito.spy(patientOrderPlaced);
        mockMvc = MockMvcBuilders.standaloneSetup(patientOrderPlaced).build();
    }
    @Test
    void patientOrders() throws Exception {
        mockMvc.perform(get("/patient/order-placed"))
                .andExpect(status().isOk())
                .andReturn();
    }
}