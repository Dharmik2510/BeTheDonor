package com.beTheDonor.controller;

import com.beTheDonor.controller.requestbody.RegistrationRequest;
import com.beTheDonor.service.ApplicationUserService;
import com.beTheDonor.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = UserRegistrationController.class)
class UserRegistrationControllerTest
{
    private MockMvc mockMvc;

    @Autowired
    UserRegistrationController userRegistrationController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private ApplicationUserService applicationUserService;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean
    private Model model;

    @MockBean
    BindingResult bindingResult;

    /*@Test
    @DisplayName("success request testing")
    @Ignore
    @Order(1)
    void testControllerSuccessRequest() throws Exception {

        RegistrationRequest registrationRequest = new RegistrationRequest("Dharmik","Soni","dhsoni2510@gmail.com","9029892923","Donor","Dharmik","Dharmik");
        Mockito.doNothing().when(registrationService).register(registrationRequest);
        userRegistrationController.register(registrationRequest,bindingResult);

    }

    @Test
    @DisplayName("Bad request testing")
    @Ignore
    @Order(2)
    void testControllerBadRequest() throws Exception {

        RegistrationRequest registrationRequest = new RegistrationRequest("Dharmik","Soni","dhsoni2510@gmail.com",null,"Donor","Dharmik","Dharmik");
        Mockito.doNothing().when(registrationService).register(registrationRequest);
        String url = "/api/v1/registration";
        mockMvc.perform(post(url).content(objectMapper.writeValueAsString(registrationRequest))).andExpect(model().hasErrors());
        userRegistrationController.register(registrationRequest,bindingResult);
    }*/

    @Test
    @DisplayName("GET Registration Page testing")
    @Order(3)
    void registrationGETTest()
    {
        userRegistrationController.registration(model);
    }


    @Test
    @DisplayName("Confirm Token testing")
    @Order(4)
    void confirmTest() throws Exception {

        String page = "confirmation";
        Mockito.doReturn(page).when(registrationService).confirmToken(any());
        userRegistrationController.confirm(any());
    }
}