package com.beTheDonor.repository;


import com.beTheDonor.entity.CreditAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CreditAmountRepository extends JpaRepository<CreditAmount,Integer> {

}
