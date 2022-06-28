package com.beTheDonor.repository;

import com.beTheDonor.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
class OrderItemsRepositoryTest
{

    @MockBean
    OrderItemsRepository orderItemsRepository;

    @MockBean
    OrderRepository orderRepository;

    @Mock
    private ApplicationUser applicationUser;

    @Mock
    DeliveryAddress deliveryAddress;

    @Mock
    Product product;

    @Test
    @Order(1)
    @DisplayName("Testing save orderItems in repository layer")
    @Rollback(value = false)
    public void saveOrderItemTest() {

        Orders orders = Orders.builder().orderId(1l).userId(applicationUser).orderPaidOn(new Date()).orderPlacedOn(new Date()).orderStatus("").orderDeliveredOn(new Date()).riderTip(10.0).total(5000.0).deliveryAddressId(deliveryAddress).build();
        orderRepository.save(orders);
        OrderItem orderItem = OrderItem.builder().orderId(orders).productId(product).id(1L).quantity(5).build();
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        Mockito.doReturn(orderItems).when(orderItemsRepository).findByOrderId(orders);
        Assertions.assertThat(orderItemsRepository.findByOrderId(orders).get(0).getOrderId()).isEqualTo(orders);
    }

    @Test
    @Order(2)
    void findByOrderId()
    {
        List<OrderItem> orderItems = new ArrayList<>();
        Orders orders = Orders.builder().build();
        Mockito.doReturn(orderItems).when(orderItemsRepository).findByOrderId(orders);
        Assertions.assertThat(orderItemsRepository.findByOrderId(orders).size()).isEqualTo(0);
    }

    @Test
    @Order(3)
    void findByOrderId2()
    {
        List<OrderItem> orderItems = new ArrayList<>();
        Orders orders = Orders.builder().orderId(1l).userId(applicationUser).orderPaidOn(new Date()).orderPlacedOn(new Date()).orderStatus("").orderDeliveredOn(new Date()).riderTip(10.0).total(500.0).deliveryAddressId(deliveryAddress).build();
        Mockito.doReturn(orderItems).when(orderItemsRepository).findByOrderId(orders);
        orderItemsRepository.findByOrderId(orders);
        Assertions.assertThat(orderItems.size()).isEqualTo(0);
    }
}