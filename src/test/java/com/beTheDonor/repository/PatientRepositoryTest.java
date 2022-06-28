package com.beTheDonor.repository;
import com.beTheDonor.entity.Patients;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class PatientRepositoryTest
{
    @Autowired
    private PatientRepository patientRepository;

    @Test
    @Order(1)
    @DisplayName("Testing patient method in repository layer")
    @Rollback(value = false)
    public void savePatientTest() {
        Patients donors = Patients.builder().id(1L).patientName("jayshree").ishelped(true).build();
        patientRepository.save(donors);
        Assertions.assertThat(donors.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    @DisplayName("Testing findAllByIshelped() method in repository layer")
    @Rollback(value = false)
    public void findAllByIshelpedTest() {
        Patients patients = new Patients();
        List<Patients> patientsList = patientRepository.findAllByIshelped(true);
        if(patientsList.size()>0){
            patients = patientsList.get(0);
        }
        Assertions.assertThat(patients.getPatientName().equals("jayshree"));
    }
}