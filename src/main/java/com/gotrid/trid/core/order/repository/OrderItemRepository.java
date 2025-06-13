package com.gotrid.trid.core.order.repository;

import com.gotrid.trid.core.order.model.OrderItem;
import com.gotrid.trid.core.order.model.OrderItemId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
    Page<OrderItem> findByVariant_Product_Shop_Owner_IdOrderByOrder_CreatedDate(Integer sellerId, Pageable pageable);
}
