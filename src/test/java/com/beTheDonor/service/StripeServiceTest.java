package com.beTheDonor.service;

import com.stripe.exception.StripeException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StripeServiceTest {


    @InjectMocks
    StripeService stripeService;

    @Test
    @Order(1)
    void createCustomerTest() throws StripeException {
        String secret_key = "sk_test_51Kk2eOEyVpasfnJhjbL6GhoTAbUe3sWaaA0oV4CMNBI0n3l51MWsFzu87N2cULiGS2WQ9BxVciCeMm7OFjVppgPB00QSaVjhf5";
        stripeService.setAPI_SECET_KEY(secret_key);
        String custom = stripeService.createCharge("test@gmail.com", "tok_mastercard", 200.0);
        Assertions.assertNotNull(custom);
        Assertions.assertEquals("ch", custom.split("_")[0]);
    }

}
