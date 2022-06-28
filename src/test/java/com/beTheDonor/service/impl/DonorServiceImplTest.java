package com.beTheDonor.service.impl;

import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.Donors;
import com.beTheDonor.entity.Orders;
import com.beTheDonor.repository.DonorRepository;
import com.beTheDonor.repository.OrderRepository;
import com.beTheDonor.repository.UserRepository;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class DonorServiceImplTest {


    @Autowired
    DonorServiceImpl donorService;
    @Mock
    JSONObject jsonObject;
    @MockBean
    Orders order;
    @Mock
    OrderRepository orderRepository;
    @Mock
    DonorRepository donorRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    Donors donor;
    @Mock
    Optional<ApplicationUser> applicationUser;

    @InjectMocks
    DonorServiceImpl donorServiceImpl;

    @Test
    @Order(1)
    @DisplayName("Check order status change")
    public void changeStatusToPendingDeliveryCheck() {

        ArrayList<Long> jsonAddress = new ArrayList<>();
        jsonAddress.add(1L);

        Mockito.doReturn(jsonAddress).when(jsonObject).get("orderId");

        Mockito.doReturn(order).when(orderRepository).getById(1L);

        Mockito.doNothing().when(order).setOrderStatus("pending delivery");

        Mockito.doReturn(order).when(orderRepository).save(order);

        Assertions.assertTrue(donorServiceImpl.changeStatusOfOrder(jsonObject));

    }


    @Test
    @Order(2)
    @DisplayName("Set total Amount donor has donated")
    public void storeTotalAmount() {
        ArrayList<Integer> jsonAddress = new ArrayList<>();
        jsonAddress.add(1);
        Integer i = 1;
        Long id = 1L;
        Mockito.doReturn(true).when(donorRepository).existsByDonorId(id);
        Mockito.doReturn(jsonAddress).when(jsonObject).get("donationAmount");
        Mockito.doReturn(donor).when(donorRepository).findByDonorId(id);
        Mockito.doNothing().when(donor).setAmount(Mockito.any());
        Mockito.doReturn(donor).when(donorRepository).save(donor);

        Assertions.assertTrue(donorServiceImpl.storeTotalAmount(jsonObject, id));


    }

    }


