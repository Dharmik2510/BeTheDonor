package com.beTheDonor.repository;

import com.beTheDonor.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Override
    Product getById(Long aLong);

    @Override
    <S extends Product> List<S> saveAll(Iterable<S> entities);

    @Override
    List<Product> findAll();

    @Override
    <S extends Product> S save(S entity);

    @Transactional
    @Modifying
    @Query("UPDATE Product a " +
            "SET a.quantity = ?1, a.price = ?2 WHERE a.productId = ?3")
    void updateProducts(int quantity, double price, long productId);
}

