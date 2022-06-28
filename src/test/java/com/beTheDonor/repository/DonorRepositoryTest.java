package com.beTheDonor.repository;
import com.beTheDonor.entity.Donors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.mockito.Mockito.doReturn;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class DonorRepositoryTest
{
    @Autowired
    private DonorRepository donorRepository;

    @Mock
    private DonorRepository donorRepositoryMock;

    @Test
    @Order(1)
    @DisplayName("Testing donor method in repository layer")
    @Rollback(value = false)
    public void saveDonorTest() {
        Donors donors = Donors.builder().id(1L).donorName("jayshree").helpDone(true).build();
        donorRepository.save(donors);
        Assertions.assertThat(donors.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    @DisplayName("Testing findAllByHelpDone() method in repository layer")
    @Rollback(value = false)
    public void findAllByHelpDoneTest() {
        Donors donors = new Donors();
        List<Donors> donorList = donorRepository.findAllByHelpDone(true);
        if(donorList.size()>0){
            donors = donorList.get(0);
        }
        Assertions.assertThat(donors.getDonorName().equals("jayshree"));
    }


    @Test
    @Order(3)
    @DisplayName("Testing selectTotals() method in repository layer")
    @Rollback(value = false)
    void selectTotalsTest()
    {
        doReturn(1D).when(donorRepositoryMock).selectTotals();
        Double total = donorRepositoryMock.selectTotals();
        System.out.println(total);
        Assertions.assertThat(total).isEqualTo(1);

    }
}