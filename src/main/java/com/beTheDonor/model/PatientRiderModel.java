package com.beTheDonor.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;

@Data
@ToString
public class PatientRiderModel
{
    private BigInteger order_id;
    private String product_name;
    private int quantity;
    private String firstname;
    private String address;
    private String city;
    private String postal_code;
    private String phone_number;


    public PatientRiderModel() {
    }

    public PatientRiderModel(BigInteger order_id, String product_name, int quantity, String firstname, String address, String city, String postal_code, String phone_number) {
        this.order_id = order_id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.firstname = firstname;
        this.address = address;
        this.city = city;
        this.postal_code = postal_code;
        this.phone_number = phone_number;
    }
}
