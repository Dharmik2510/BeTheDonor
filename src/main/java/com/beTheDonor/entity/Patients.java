package com.beTheDonor.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="patients")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patients {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "patient_name", nullable = false)
	private String patientName;

	@Column(name = "is_helped")
	private Boolean ishelped;

	@Column(name = "email")
	private String emailId;

	@Column(name = "enabled")
	private Boolean status;
}
