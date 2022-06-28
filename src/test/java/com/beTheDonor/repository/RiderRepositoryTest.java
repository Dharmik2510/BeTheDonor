package com.beTheDonor.repository;

import com.beTheDonor.entity.Riders;
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
class RiderRepositoryTest {
    @Autowired
    private RiderRepository riderRepository;

    @Test
    @Order(1)
    @DisplayName("Testing riders method in repository layer")
    @Rollback(value = false)
    public void saveRidersTest() {
        Riders riders = Riders.builder().id(1L).driverName("Dharmik").delivery(true).build();
        riderRepository.save(riders);
        Assertions.assertThat(riders.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    @DisplayName("Testing findAllByDelivery() method in repository layer")
    @Rollback(value = false)
    public void findAllByDeliveryTest() {
        Riders riders = new Riders();
        List<Riders> ridersList = riderRepository.findAllByDelivery(true);
        if (ridersList.size() > 0) {
            riders = ridersList.get(0);
        }
        Assertions.assertThat(riders.getDriverName().equals("Dharmik"));
    }

    @Test
    @Order(3)
    @DisplayName("Testing findAll() methof")
    @Rollback(value = false)
    void findAll() {
        Riders riders = Riders.builder().id(1L).driverName("Dharmik").delivery(true).build();
        riderRepository.save(riders);
        List<Riders> ridersList = riderRepository.findAll();
        Assertions.assertThat(ridersList.size()).isEqualTo(1);
    }
}