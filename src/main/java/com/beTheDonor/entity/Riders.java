package com.beTheDonor.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="riders")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Riders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "driver_name", nullable = false)
	private String driverName;

	@Column(name = "delivery")
	private Boolean delivery;

	@Column(name = "email")
	private String emailId;

	@Column(name = "enabled")
	private Boolean status;
}
