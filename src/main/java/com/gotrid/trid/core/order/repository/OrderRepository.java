package com.gotrid.trid.core.order.repository;

import com.gotrid.trid.api.admin.dto.RecentOrderDto;
import com.gotrid.trid.core.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByCustomer_Id(Integer customerId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(o.total_amount), 0) FROM Order o WHERE o.status = 'COMPLETED'")
    BigDecimal sumTotalAmount();

    @Query("""
            SELECT new com.gotrid.trid.api.admin.dto.RecentOrderDto(
                o.id, u.firstname, o.total_amount, o.createdDate
            )
            FROM Order o
            JOIN o.customer u
            """)
    List<RecentOrderDto> getRecentOrders(Pageable pageable);
}
