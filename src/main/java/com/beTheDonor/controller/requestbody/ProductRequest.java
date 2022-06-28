package com.beTheDonor.controller.requestbody;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ProductRequest {
    public ProductRequest() {

    }
    private  int productId;
    private String productName;
    private int quantity;
    private Double price;
    private String category;
    private String comment;
}
