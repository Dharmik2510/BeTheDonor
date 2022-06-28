package com.beTheDonor.service.impl;

import com.beTheDonor.entity.CreditAmount;
import com.beTheDonor.entity.Orders;
import com.beTheDonor.repository.CreditAmountRepository;
import com.beTheDonor.repository.OrderRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class CreditAmountServiceImplTest {

    @Mock
    CreditAmountRepository creditAmountRepository;

    @MockBean
    CreditAmount creditAmount;

    @Mock
    OrderRepository orderRepository;

    @MockBean
    Orders order;

    @InjectMocks
    CreditAmountServiceImpl creditAmountService;

    @Test
    void orderFromCredits() {
        Orders order = Orders.builder().total(100.0).build();
        CreditAmount creditAmount = CreditAmount.builder().id(1).creditAmount(1000.0).riderTipPercent(10.0).fundRaised(2000.0).build();
        CreditAmount creditAmountUpdated = CreditAmount.builder().creditAmount(890.0).build();
        creditAmountRepository.save(creditAmount);

        List<Orders> orders = new ArrayList<>();
        orders.add(order);
        Mockito.doReturn(creditAmount).when(creditAmountRepository).getById(1);

        Mockito.when(orderRepository.findByOrderStatusAndTotalLessThanEqual("pending payment",900.0)).thenReturn(orders);

        Mockito.doReturn(creditAmountUpdated).when(creditAmountRepository).save(creditAmountUpdated);
        Mockito.when(orderRepository.save(orders.get(0))).thenReturn(order);

        creditAmountService.orderFromCredits();
        Assertions.assertEquals(creditAmountUpdated.getCreditAmount(),creditAmount.getCreditAmount());


    }

    @Test
    void updateTipPercent() {
        Double tip = 20.0;
        CreditAmount creditAmountOld = CreditAmount.builder().riderTipPercent(10.0).build();
        CreditAmount creditAmountNew = CreditAmount.builder().riderTipPercent(20.0).build();
        Mockito.doReturn(creditAmountOld).when(creditAmountRepository).getById(1);
        Mockito.doNothing().when(creditAmount).setRiderTipPercent(tip);
        Mockito.doReturn(creditAmountNew).when(creditAmountRepository).save(creditAmountNew);
        creditAmountService.updateTipPercent(tip);
        Assertions.assertEquals(tip,creditAmountOld.getRiderTipPercent());
    }

    @Test
    void updateCreditAmount() {
        /*Double amount = 500.0;
        creditAmountRepository.save(creditAmount);
        orderFromCredits();

        CreditAmount creditAmountOld = CreditAmount.builder().creditAmount(100.0).build();
        CreditAmount creditAmountNew = CreditAmount.builder().creditAmount(600.0).build();
        Mockito.doReturn(creditAmountOld).when(creditAmountRepository).getById(1);
        Mockito.doNothing().when(creditAmount).setRiderTipPercent(amount);
        Mockito.doReturn(creditAmountNew).when(creditAmountRepository).save(creditAmountNew);
        Mockito.doNothing().when(creditAmount).setFundRaised(Mockito.any());
        Mockito.doNothing().when(cr
        creditAmountService.setFundRaised(Mockito.any());
        Assertions.assertEquals(tip,creditAmountOld.getRiderTipPercent());*/
    }
}