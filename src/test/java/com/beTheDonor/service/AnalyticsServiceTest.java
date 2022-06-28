package com.beTheDonor.service;

import com.beTheDonor.entity.Donors;
import com.beTheDonor.entity.Patients;
import com.beTheDonor.entity.Riders;
import com.beTheDonor.repository.DonorRepository;
import com.beTheDonor.repository.PatientRepository;
import com.beTheDonor.repository.RiderRepository;
import com.beTheDonor.service.impl.AnalyticsServiceImpl;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnalyticsServiceTest {


    @InjectMocks
    AnalyticsServiceImpl analyticsService;

    @Mock
    private DonorRepository donorRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private RiderRepository riderRepository;

    @Test
    @DisplayName("Testing getAllHelpedPatients Function")
    @Order(1)
    void getAllHelpedPatientsTest() {
        Mockito.doReturn(Arrays.asList(new Patients(1L,"jayashree",true,"jayashree1@gmail.com",true),new Patients (2L,"jayashree2",true,"jayashree2@gmail.com",true))).when(patientRepository).findAllByIshelped(true);
        List<Patients> patientsList = analyticsService.getAllHelpedPatients();
        Assertions.assertEquals("jayashree",patientsList.get(0).getPatientName());
    }

   /* @Test
    @DisplayName("Testing getAllDonorsWhoHelped Function")
    @Order(2)
    @Ignore
    void getAllDonorsWhoHelpedTest() {
        Mockito.doReturn(Arrays.asList(new Donors(1L,1L,"jayshree",28L,5000D,true))).when(donorRepository).findAllByHelpDone(true);
        List<Donors> donorsList = analyticsService.getAllDonorsWhoHelped();
        Assertions.assertEquals("jayshree", donorsList.get(0).getDonorName());
    }*/

    @Test
    @DisplayName("Testing getAllRidersWhoDeliver Function")
    @Order(3)
    void getAllRidersWhoDeliverTest() {
        Mockito.doReturn(Arrays.asList(new Riders(1L,"jayashree",true,"jayashree3@gmal.com",true))).when(riderRepository).findAllByDelivery(true);
        List<Riders> riderList = analyticsService.getAllRidersWhoDeliver();
        Assertions.assertEquals("jayashree", riderList.get(0).getDriverName());
    }

    @Test
    @DisplayName("Testing totalAmountOfHelp Function")
    @Order(4)
    void totalAmountOfHelpTest() {
        Mockito.doReturn(12000D).when(donorRepository).selectTotals();
        Double amount = analyticsService.totalAmountOfHelp();
        Assertions.assertEquals(12000D,amount);
    }
}
