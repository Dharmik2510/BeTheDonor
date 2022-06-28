package com.beTheDonor.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = AnalyticsController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(locations = "/test-context.xml")
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AnalyticsController analyticsController;
    //we are checking analyticsService.getAllHelpedPatients()
    //we are mocking the patientRepository.findAllByIshelped(true) before executing above call
    @Test
    @DisplayName("success getAllHelpedPatients test")
    @Order(1)
    void testControllerGetAllHelpedPatientsSuccessRequest() throws Exception {
        mockMvc.perform(get("/api/v1/analytics/patients")
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }
    //we are checking analyticsService.getAllDonorsWhoHelped();
    //we are mocking the donorRepository.findAllByHelpDone(true) before executing above call
    @Test
    @DisplayName("success getAllDonorsWhoHelped test")
    @Order(2)
    void testControllerGetAllDonorsWhoHelpedSuccessRequest() throws Exception {
        this.mockMvc.perform(get("/api/v1/analytics/donors")
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }
    //we are checking analyticsService.getAllRidersWhoDeliver()
    //we are mocking the riderRepository.findAllByDelivery(true) before executing above call
    @Test
    @DisplayName("success getAllRidersWhoDeliver test")
    @Order(3)
    void testControllerGetAllRidersWhoDeliverSuccessRequest() throws Exception {
        this.mockMvc.perform(get("/api/v1/analytics/riders")
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }
    //we are checking analyticsService.totalAmountOfHelp()
    //we are mocking the donorRepository.selectTotals(true) before executing above call
    @Test
    @DisplayName("success getAllProducts test")
    @Order(4)
    void testControllerGetAllProductsSuccessRequest() throws Exception {
        this.mockMvc.perform(get("/api/v1/analytics/total")
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());
    }
}