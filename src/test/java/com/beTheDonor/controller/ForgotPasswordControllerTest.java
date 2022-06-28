package com.beTheDonor.controller;

import com.beTheDonor.email.EmailSender;
import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.service.ApplicationUserService;
import com.beTheDonor.util.Utility;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.beTheDonor.util.Utility.getUrl;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(ForgotPasswordController.class)
class ForgotPasswordControllerTest {

    private MockMvc mockMvc;

    @Autowired
    ForgotPasswordController forgotPasswordController;

    @MockBean
    ApplicationUserService applicationUserService;

    @MockBean
    JavaMailSender emailSender;

    @MockBean
    BCryptPasswordEncoder passwordEncoder;

    @MockBean
    MimeMessage message;

    @MockBean
    Model model;

    @MockBean
    HttpServletRequest request;

    @MockBean
    Utility utility;

    @BeforeEach
    void setUp() {
        forgotPasswordController = Mockito.spy(forgotPasswordController);
        mockMvc = MockMvcBuilders.standaloneSetup(forgotPasswordController).build();
    }

    @Test
    @DisplayName("Testing showForgotPasswordForm")
    @Order(1)
    void showForgotPasswordForm() throws Exception {
        mockMvc.perform(get("/forgotPassword"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("forgot_password"));

        forgotPasswordController.showForgotPasswordForm();
    }

    @Test
    @Order(2)
    void processForgotPassword() throws Exception {
        String email = "dhsoni26510@gmail.com";
        String token = "abcdeggh";
        String resetpasswordLink = "abcd";


        Mockito.doNothing().when(forgotPasswordController).sendEmail(Mockito.anyString(),Mockito.anyString());

        Mockito.when(request.getParameter("email")).thenReturn(email);

        Mockito.doNothing().when(applicationUserService).updateResetPasswordToken(token,email);


        try(MockedStatic<Utility> utilities = Mockito.mockStatic(Utility.class))
        {
            utilities.when(()->Utility.getUrl(request)).thenReturn(resetpasswordLink);
        }

        mockMvc.perform(post("/forgotPassword",request,model).contentType(MediaType.APPLICATION_JSON)
                .param("email","dhsoni2510@gmail.com"))
                .andDo(print())
                .andExpect(view().name("forgot_password"));

    }

    @Test
    @Order(3)
    @DisplayName("Testing showPasswordResetForm() when ApplicationUser is not null")
    void showResetPasswordFormUserNotNull() throws Exception {
        String token = "abcd";
        ApplicationUser applicationUser = new ApplicationUser();
        Mockito.doReturn(applicationUser).when(applicationUserService).getByResetPasswordToken(token);
        mockMvc.perform(get("/reset_password",model).param("token","abcd"))
                .andDo(print())
                .andExpect(view().name("resetPassword"));
    }


    @Test
    @Order(4)
    @DisplayName("Testing showPasswordResetForm() when ApplicationUser is null")
    void showResetPasswordFormUserNull() throws Exception {
        String token = "abcd";
        ApplicationUser applicationUser = new ApplicationUser();
        Mockito.doReturn(applicationUser).when(applicationUserService).getByResetPasswordToken("ab");
        mockMvc.perform(get("/reset_password",model).param("token","abcd"))
                .andDo(print())
                .andExpect(view().name("message"));
    }

    @Test
    @Order(5)
    @DisplayName("Testing processResetPassword() method when Application User not null")
    void processResetPasswordUserNotNull() throws Exception {


        ApplicationUser applicationUser = new ApplicationUser();
        Mockito.doReturn(applicationUser).when(applicationUserService).getByResetPasswordToken("abc");
        Mockito.doNothing().when(applicationUserService).updatePassword(applicationUser,"abc");
        mockMvc.perform(post("/reset_password",request,model).contentType(MediaType.APPLICATION_JSON)
                .param("token","abc")
                .param("password","abc")
                ).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/api/v1/login"));
    }

    @Test
    @Order(6)
    @DisplayName("Testing processResetPassword() method when Application User not null")
    void processResetPasswordUserNull() throws Exception {


        ApplicationUser applicationUser = new ApplicationUser();
        Mockito.doReturn(applicationUser).when(applicationUserService).getByResetPasswordToken("ab");
        Mockito.doNothing().when(applicationUserService).updatePassword(applicationUser,"ab");
        mockMvc.perform(post("/reset_password",request,model).contentType(MediaType.APPLICATION_JSON)
                        .param("token","abc")
                        .param("password","abc")
                ).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/resetPassword"));
    }

    @Test
    @DisplayName("Testing sendEmail() method")
    @Order(7)
    void sendEmail() throws MessagingException, UnsupportedEncodingException {
        String receiver_mail = "dhsoni2510@gmail.com";
        String resetPasswordLink = "";

        Mockito.doReturn(message).when(emailSender).createMimeMessage();
        Mockito.doNothing().when(emailSender).send(message);

        forgotPasswordController.sendEmail(receiver_mail,resetPasswordLink);

    }
}