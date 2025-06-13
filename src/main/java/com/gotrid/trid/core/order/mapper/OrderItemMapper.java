package com.gotrid.trid.core.order.mapper;

import com.gotrid.trid.api.order.dto.OrderSellerResponse;
import com.gotrid.trid.core.order.model.OrderItem;
import com.gotrid.trid.core.product.mapper.ProductVariantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class OrderItemMapper implements Function<OrderItem, OrderSellerResponse> {
    private final ProductVariantMapper productVariantMapper;

    @Override
    public OrderSellerResponse apply(OrderItem item) {
        return new OrderSellerResponse(
                item.getOrder().getId(),
                item.getVariant().getProduct().getId(),
                productVariantMapper.toResponse(item.getVariant()),
                item.getQuantity(),
                item.getOrder().getStatus()
        );
    }
}
