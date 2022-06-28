package com.beTheDonor.controller;

import com.beTheDonor.service.ApplicationUserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(UserLoginController.class)
        /*@ActiveProfiles("test")*/
class UserLoginControllerTest {

    @MockBean
    Model model;
    @Autowired
    UserLoginController userLoginController;
    @MockBean
    BCryptPasswordEncoder passwordEncoder;
    private MockMvc mockMvc;
    @MockBean
    private DaoAuthenticationProvider authenticationManager;
    @MockBean
    private ApplicationUserService userDetailsService;

    @BeforeEach
    void setUp() {
        userLoginController = Mockito.spy(userLoginController);
        mockMvc = MockMvcBuilders.standaloneSetup(userLoginController).build();
    }

    @Test
    @Order(1)
    @DisplayName("Authenticate User Donor")
    void checkAuthenticationDonor() throws Exception {

        Authentication authentication = new UsernamePasswordAuthenticationToken("dhsoni2510@gmail.com", "123456", AuthorityUtils.createAuthorityList("Donor"));

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        Mockito.when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new User("dhsoni2510@gmail.com", "123456", AuthorityUtils.createAuthorityList("Donor")));

        mockMvc.perform(post("/api/v1/authenticate")
                        .param("email", "dhsoni2510@gmail.com")
                        .param("password", "123456"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/donorview"));

    }

    @Test
    @Order(2)
    @DisplayName("Authenticate User Rider")
    void checkAuthenticationPatient() throws Exception {

        Authentication authentication = new UsernamePasswordAuthenticationToken("dhsoni2510@gmail.com", "123456", AuthorityUtils.createAuthorityList("Patient"));

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        Mockito.when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new User("dhsoni2510@gmail.com", "123456", AuthorityUtils.createAuthorityList("Patient")));

        mockMvc.perform(post("/api/v1/authenticate")
                        .param("email", "dhsoni2510@gmail.com")
                        .param("password", "123456"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/dashboard"));

    }

    @Test
    @Order(3)
    @DisplayName("Authenticate User Patient")
    void checkAuthenticationRider() throws Exception {

        Authentication authentication = new UsernamePasswordAuthenticationToken("dhsoni2510@gmail.com", "123456", AuthorityUtils.createAuthorityList("Rider"));

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        Mockito.when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new User("dhsoni2510@gmail.com", "123456", AuthorityUtils.createAuthorityList("Rider")));

        mockMvc.perform(post("/api/v1/authenticate")
                        .param("email", "dhsoni2510@gmail.com")
                        .param("password", "123456"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/riderDashboard"));

    }

    @Test
    @Order(4)
    @DisplayName("Redirect to html page for logout")
    public void redirectToLoginForLogout() throws Exception {
        mockMvc.perform(get("/logoutSuccessful", model))
                .andDo(print())
                .andExpect(view().name("redirect:/api/v1/login"));

    }

    @Test
    @Order(5)
    @DisplayName("Redirect to html page for access denied")
    public void redirectToLoginForaccessDenied() throws Exception {
        mockMvc.perform(get("/acessdenied", model))
                .andDo(print())
                .andExpect(view().name("redirect:/api/v1/login"));

    }


}