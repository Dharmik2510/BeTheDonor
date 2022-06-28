package com.beTheDonor.controller;

import com.beTheDonor.dto.PatientRiderDto;
import com.beTheDonor.model.PatientRiderModel;
import com.beTheDonor.repository.OrderRepository;
import com.beTheDonor.service.ApplicationUserService;
import com.beTheDonor.service.impl.RiderService;
import com.beTheDonor.util.Utility;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = RiderController.class)
@AutoConfigureMockMvc
class RiderControllerTest {


    private MockMvc mockMvc;

    @Autowired
    RiderController riderController;

    @MockBean
    RiderService riderService;

    @MockBean
    Model model;

    @MockBean
    ApplicationUserService applicationUserService;

    @MockBean
    BCryptPasswordEncoder passwordEncoder;

    @MockBean
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        riderController = Mockito.spy(riderController);
        mockMvc = MockMvcBuilders.standaloneSetup(riderController).build();
    }

    @Test
    @Order(1)
    @DisplayName("testing getRiderDashboard() when list is not empty")
    void getRiderDashboardWhenListNotEmpty() throws Exception {

        ArrayList<PatientRiderModel> patientRiderModels = new ArrayList<>();
        patientRiderModels.add(new PatientRiderModel());
        ArrayList<PatientRiderDto> patientRiderDtos = new ArrayList<>();
        patientRiderDtos.add(new PatientRiderDto());
        Mockito.when(riderService.getByCityName("Halifax")).thenReturn(patientRiderModels);


        try(MockedStatic<PatientRiderDto> utilities = Mockito.mockStatic(PatientRiderDto.class))
        {
            utilities.when(()->PatientRiderDto.convertToDto(patientRiderModels)).thenReturn(patientRiderDtos);
        }

        mockMvc.perform(get("/riderDashboard/city",model).param("cityName","Halifax"))
                .andDo(print())
                .andExpect(view().name("riderDashboard"));

    }

    @Test
    @Order(2)
    @DisplayName("testing getRiderDashboard() when list is empty")
    void getRiderDashboardWhenListIsEmpty() throws Exception {

        ArrayList<PatientRiderModel> patientRiderModels = new ArrayList<>();
        Mockito.when(riderService.getByCityName("Halifax")).thenReturn(patientRiderModels);


        mockMvc.perform(get("/riderDashboard/city",model).param("cityName","Halifax"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/riderDashboard?noPatient"));

    }


    @Test
    @Order(3)
    void getRiders() throws Exception {
        List<String> cities = new ArrayList<>();
        cities.add("halifax");
        Mockito.when(riderService.getCities()).thenReturn(cities);
        mockMvc.perform(get("/riderDashboard/keyword").param("q","ha")
                .contentType(MediaType.APPLICATION_JSON).content("").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Mockito.verify(riderController).getRiders("ha");
    }
}