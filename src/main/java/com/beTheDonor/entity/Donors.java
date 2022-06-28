package com.beTheDonor.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "donors")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Donors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "donorId")
    private Long donorId;

    @Column(name = "donor_name", nullable = false)
    private String donorName;

    @Column(name = "email")
    private String emailId;

    @Column(name = "enabled")
    private Boolean status;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "help_done")
    private Boolean helpDone;
}
