package com.gotrid.trid.core.order.mapper;

import com.gotrid.trid.api.order.dto.OrderResponse;
import com.gotrid.trid.core.order.model.Order;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OrderMapper implements Function<Order, OrderResponse> {

    @Override
    public OrderResponse apply(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotal_amount(),
                order.getStatus(),
                order.getOrderItems().stream()
                        .map(item -> item.getVariant().getId())
                        .collect(Collectors.toSet())
        );
    }
}
