package com.beTheDonor.repository;

import java.util.List;

import com.beTheDonor.entity.Riders;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RiderRepository extends JpaRepository<Riders, Long> {
	List<Riders> findAllByDelivery(Boolean flag);
	List<Riders> findAll();
	List<Riders> findAllByEmailId(String email);
	List<Riders> findAllByStatus(boolean b);
	List<Riders> findAllByDelivery(boolean b);
}
