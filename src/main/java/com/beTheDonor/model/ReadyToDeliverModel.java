package com.beTheDonor.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.parameters.P;

import java.math.BigInteger;

@Data
@ToString
public class ReadyToDeliverModel
{
    private BigInteger order_id;
    private String address;
    private String firstname;
    private String phone_number;


    public ReadyToDeliverModel() {

    }

    public ReadyToDeliverModel(BigInteger order_id, String address, String firstname, String phone_number) {
        this.order_id = order_id;
        this.address = address;
        this.firstname = firstname;
        this.phone_number = phone_number;
    }
}
