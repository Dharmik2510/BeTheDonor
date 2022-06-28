package com.beTheDonor.service.impl;

import com.beTheDonor.entity.CreditAmount;
import com.beTheDonor.entity.Orders;
import com.beTheDonor.repository.CreditAmountRepository;
import com.beTheDonor.repository.OrderRepository;
import com.beTheDonor.service.CreditAmountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CreditAmountServiceImpl implements CreditAmountService {

    @Autowired
    CreditAmountRepository creditAmountRepository;

    @Autowired
    OrderRepository orderRepository;

    @Override
    public void orderFromCredits() {

        CreditAmount creditAmount = creditAmountRepository.getById(1);
        Double currentAmount = creditAmount.getCreditAmount();
        Double tip = creditAmountRepository.getById(1).getRiderTipPercent();
        List<Orders> orders = orderRepository.findByOrderStatusAndTotalLessThanEqual("pending payment",currentAmount-(currentAmount*tip/100));
        for (int i = 0; i < orders.size(); i++) {
            int getPercentage = 100;
            if (currentAmount >= orders.get(i).getTotal() + tip) {
                currentAmount -= orders.get(i).getTotal() + tip;
                creditAmount.setCreditAmount(currentAmount);
                orders.get(i).setOrderStatus("pending delivery");
                orders.get(i).setOrderPaidOn(new Date());
                orders.get(i).setRiderTip(creditAmount.getRiderTipPercent() * orders.get(i).getTotal() / getPercentage);
                creditAmountRepository.save(creditAmount);
                orderRepository.save(orders.get(i));
            }
        }
    }

    @Override
    public void updateTipPercent(Double tip) {
        CreditAmount creditAmount = creditAmountRepository.getById(1);
        creditAmount.setRiderTipPercent(tip);
        creditAmountRepository.save(creditAmount);
    }

    @Override
    public void updateCreditAmount(Double amount) {
        CreditAmount creditAmount = creditAmountRepository.getById(1);
        creditAmount.setCreditAmount(creditAmount.getCreditAmount() + amount);
        creditAmount.setFundRaised(creditAmount.getFundRaised() + amount);
        creditAmountRepository.save(creditAmount);
        orderFromCredits();
    }
}