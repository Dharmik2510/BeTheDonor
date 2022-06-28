package com.beTheDonor.repository;

import com.beTheDonor.entity.OrderItem;
import com.beTheDonor.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByOrderId(Orders order);
}
