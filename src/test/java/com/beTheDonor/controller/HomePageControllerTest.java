package com.beTheDonor.controller;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class HomePageControllerTest {
    @MockBean
    Model model;
    @Autowired
    HomePageController homePageController;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        homePageController = Mockito.spy(homePageController);
        mockMvc = MockMvcBuilders.standaloneSetup(homePageController).build();
    }

    @Test
    @Order(1)
    @DisplayName("Redirect to html page")
    public void redirectToHomePage() throws Exception {
        mockMvc.perform(get("/homePage", model))
                .andDo(print())
                .andExpect(view().name("HomePage"));

    }

}