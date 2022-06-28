package com.beTheDonor.service;

import org.springframework.stereotype.Service;

@Service
public interface CreditAmountService {
    void orderFromCredits();
    void updateTipPercent(Double tip);
    void updateCreditAmount(Double amount);
}
