package com.beTheDonor.service;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StripeService {

    @Value("${STRIPE_SECRET_KEY}")
    private String API_SECET_KEY;

    public String createCharge(String email, String token, double amount) {

        String chargeId = null;

        try {
            Stripe.apiKey = API_SECET_KEY;

            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("description","Charge for "+email);
            chargeParams.put("currency","usd");
            chargeParams.put("amount",(int)amount);
            chargeParams.put("source",token);

            Charge charge = Charge.create(chargeParams);

            chargeId = charge.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeId;
    }

}
