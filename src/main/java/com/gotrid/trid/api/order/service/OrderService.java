package com.gotrid.trid.api.order.service;

import com.gotrid.trid.api.order.dto.OrderResponse;
import com.gotrid.trid.api.order.dto.OrderSellerResponse;
import com.gotrid.trid.common.exception.custom.UnAuthorizedException;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.core.order.mapper.OrderItemMapper;
import com.gotrid.trid.core.order.mapper.OrderMapper;
import com.gotrid.trid.core.order.model.Order;
import com.gotrid.trid.core.order.model.OrderItem;
import com.gotrid.trid.core.order.repository.OrderItemRepository;
import com.gotrid.trid.core.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository itemRepository;

    @Cacheable(value = "orderHistory", key = "#userId + '-' + #page + '-' + #size",
            condition = "#size <= 20",
            unless = "#result.content.isEmpty()")
    public PageResponse<OrderResponse> getOrders(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<Order> orderPage = orderRepository.findByCustomer_Id(userId, pageable);

        List<OrderResponse> responses = orderPage.map(orderMapper).getContent();

        return new PageResponse<>(
                responses,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isFirst(),
                orderPage.isLast()
        );
    }

    public OrderResponse getOrder(Integer userId, Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getCustomer().getId().equals(userId)) {
            throw new UnAuthorizedException("You are not authorized to view this order");
        }

        return orderMapper.apply(order);
    }

    @Cacheable(value = "sellerOrders", key = "#sellerId + '-' + #page + '-' + #size",
            condition = "#size <= 50",
            unless = "#result.content.isEmpty()")
    public PageResponse<OrderSellerResponse> getSellerOrders(Integer sellerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<OrderItem> orderItemsPage = itemRepository.findByVariant_Product_Shop_Owner_IdOrderByOrder_CreatedDate(sellerId, pageable);

        List<OrderSellerResponse> responses = orderItemsPage.map(orderItemMapper).getContent();

        return new PageResponse<>(
                responses,
                orderItemsPage.getNumber(),
                orderItemsPage.getSize(),
                orderItemsPage.getTotalElements(),
                orderItemsPage.getTotalPages(),
                orderItemsPage.isFirst(),
                orderItemsPage.isLast()
        );
    }
}
