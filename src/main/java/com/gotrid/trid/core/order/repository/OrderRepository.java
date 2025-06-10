package com.gotrid.trid.core.order.repository;

import com.gotrid.trid.core.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
