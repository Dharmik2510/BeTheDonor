package com.beTheDonor.controller.pages;

import com.beTheDonor.service.ApplicationUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
@WebMvcTest(controllers = riderDashboard.class)
@AutoConfigureMockMvc
class riderDashboardTest {
    private MockMvc mockMvc;

    @Autowired
    riderDashboard riderDashboard;

    @MockBean
    ApplicationUserService applicationUserService;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        riderDashboard = Mockito.spy(riderDashboard);
        mockMvc = MockMvcBuilders.standaloneSetup(riderDashboard).build();
    }
    @Test
    void getRiderPage() throws Exception {
        mockMvc.perform(get("/riderDashboard"))
                .andExpect(status().isOk())
                .andReturn();
    }
}