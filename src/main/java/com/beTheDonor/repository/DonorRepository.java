package com.beTheDonor.repository;

import java.util.List;

import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.Donors;
import com.beTheDonor.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// api for CRUD operations like create, read, update and delete
@Repository
public interface DonorRepository extends JpaRepository<Donors, Long> {

    List<Donors> findAllByHelpDone(Boolean flag);

    Boolean existsByDonorId(Long Id);

    List<Donors> findAll();


    @Query("SELECT SUM(m.amount) FROM Donors m where m.helpDone = true")
    Double selectTotals();

    List<Donors> findAllByEmailId(String email);

    List<Donors> findAllByStatus(boolean b);

    Donors findByDonorId(Long id);
}
