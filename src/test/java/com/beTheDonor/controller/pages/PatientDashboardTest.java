package com.beTheDonor.controller.pages;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = PatientDashboard.class)
@AutoConfigureMockMvc
class PatientDashboardTest {
    private MockMvc mockMvc;

    @Autowired
    PatientDashboard patientDashboard;

    @MockBean
    ApplicationUserService applicationUserService;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        patientDashboard = Mockito.spy(patientDashboard);
        mockMvc = MockMvcBuilders.standaloneSetup(patientDashboard).build();
    }

    @Test
    void deliveryAddress() throws Exception {

        mockMvc.perform(get("/patient/dashboard"))
                .andExpect(status().isOk())
                .andReturn();
    }

}