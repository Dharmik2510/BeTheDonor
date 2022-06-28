package com.beTheDonor.repository;

import com.beTheDonor.entity.*;
import com.beTheDonor.model.PatientRiderModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class OrderRepositoryTest {

    @MockBean
    OrderRepository orderRepository;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    DeliveryAddress deliveryAddress;

    @Test
    @Order(1)
    @DisplayName("Testing save order in repository layer")
    @Rollback(value = false)
    public void saveOrderTest() {
        Orders orders = Orders.builder().orderId(1l).userId(applicationUser).orderPaidOn(new Date()).orderPlacedOn(new Date()).orderStatus("").orderDeliveredOn(new Date()).riderTip(10.0).total(5000.0).deliveryAddressId(deliveryAddress).build();
        orderRepository.save(orders);
        ApplicationUser applicationUser = ApplicationUser.builder().build();
        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(orders);
        Mockito.doReturn(ordersList).when(orderRepository).findByUserId(applicationUser);
        Assertions.assertThat(orderRepository.findByUserId(applicationUser).get(0).getUserId().getId().equals(1L));
    }

    @Test
    void findByUserId() {
        List<Orders> orders = new ArrayList<>();
        ApplicationUser applicationUser = ApplicationUser.builder().build();
        Mockito.doReturn(orders).when(orderRepository).findByUserId(applicationUser);
        Assertions.assertThat(orderRepository.findByUserId(applicationUser).size()).isEqualTo(0);
    }

    @Test
    void findByOrderStatusAndTotalLessThanEqual() {
        Orders order1= Orders.builder().build();
        Orders order2= Orders.builder().build();
        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(order1);
        ordersList.add(order2);
        Mockito.doReturn(ordersList).when(orderRepository).findByOrderStatusAndTotalLessThanEqual("",5000.0);
        Assertions.assertThat(orderRepository.findByOrderStatusAndTotalLessThanEqual("",5000.0)).size().isEqualTo(2);
    }

    @Test
    void findCities()
    {
        List<String> cityList = new ArrayList<>();
        cityList.add("Halifax");
        Mockito.when(orderRepository.findCities()).thenReturn(cityList);
        orderRepository.findCities();
    }

    @Test
    void getByCityName()
    {
        List<PatientRiderModel> patientRiderModels = new ArrayList<>();
        patientRiderModels.add(new PatientRiderModel());
        String cityName = "Halifax";
        Mockito.when(orderRepository.getByCityName(cityName)).thenReturn(patientRiderModels);
        orderRepository.getByCityName(cityName);
    }
}
