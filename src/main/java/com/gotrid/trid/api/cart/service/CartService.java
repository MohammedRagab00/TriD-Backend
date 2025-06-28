package com.gotrid.trid.api.cart.service;

import com.gotrid.trid.api.cart.dto.CartResponse;
import com.gotrid.trid.cache.RedisWildcardEvict;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.core.address.model.Address;
import com.gotrid.trid.core.address.repository.AddressRepository;
import com.gotrid.trid.core.cart.mapper.CartItemMapper;
import com.gotrid.trid.core.cart.model.Cart;
import com.gotrid.trid.core.cart.model.CartItem;
import com.gotrid.trid.core.cart.model.CartItemId;
import com.gotrid.trid.core.cart.repository.CartItemRepository;
import com.gotrid.trid.core.cart.repository.CartRepository;
import com.gotrid.trid.core.order.model.Order;
import com.gotrid.trid.core.order.model.OrderItem;
import com.gotrid.trid.core.order.model.OrderItemId;
import com.gotrid.trid.core.order.repository.OrderRepository;
import com.gotrid.trid.core.product.model.ProductVariant;
import com.gotrid.trid.core.product.repository.ProductVariantRepository;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.gotrid.trid.core.order.model.Status.PENDING;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository variantRepository;
    private final CartItemMapper cartItemMapper;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    @Transactional
    @RedisWildcardEvict(cacheName = "userCarts", keyPrefix = "#userId")
    public void addToCart(Integer variantId, Integer quantity, Integer userId) {
        Users user = getUser(userId);
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new EntityNotFoundException("Product variant not found with id: " + variantId));

        if (quantity > variant.getStock()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }

        Cart cart = cartRepository.findByUser(user).orElseGet(
                () -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return newCart;
                }
        );

        Optional<CartItem> cartItem = cart.getCartItem()
                .stream()
                .filter(item -> item.getVariant().equals(variant))
                .findFirst();

        if (cartItem.isPresent()) {
            cartItem.get().setQuantity(quantity);
        } else {
            cart.getCartItem().add(
                    CartItem.builder()
                            .id(new CartItemId(cart.getId(), variant.getId()))
                            .cart(cart)
                            .variant(variant)
                            .quantity(quantity)
                            .build()
            );
        }

        cartRepository.save(cart);
    }

    @Transactional
    @RedisWildcardEvict(cacheName = "userCarts", keyPrefix = "#userId")
    public void removeFromCart(Integer variantId, Integer userId) {
        Users user = getUser(userId);
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new EntityNotFoundException("Product variant not found with id: " + variantId));

        Cart cart = getCart(user);

        Optional<CartItem> cartItem = cart.getCartItem()
                .stream()
                .filter(item -> item.getVariant().equals(variant))
                .findFirst();

        cartItem.ifPresent(cart.getCartItem()::remove);
        if (cart.getCartItem().isEmpty()) {
            cartRepository.delete(cart);
        } else {
            cartRepository.save(cart);
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userCarts", key = "#userId + '-' + #page + '-' + #size",
            condition = "#size <= 20",
            unless = "#result.content.isEmpty()")
    public PageResponse<CartResponse> getCart(int page, int size, Integer userId) {
        Pageable pageable = PageRequest.of(page, size);

        Page<CartItem> cartPage = cartItemRepository.findByCart_User_Id(userId, pageable);

        List<CartResponse> responses = cartPage.map(cartItemMapper).getContent();

        return new PageResponse<>(
                responses,
                cartPage.getNumber(),
                cartPage.getSize(),
                cartPage.getTotalElements(),
                cartPage.getTotalPages(),
                cartPage.isFirst(),
                cartPage.isLast()
        );

    }

    @Transactional
    @RedisWildcardEvict(cacheName = "userCarts", keyPrefix = "#userId")
    public Integer checkout(Integer userId) {
        Users user = getUser(userId);
        Address address = addressRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Address not found for user with id: " + userId));
        Cart cart = getCart(user);
        validateCart(cart);

        Order order = new Order();
        addAddressToOrder(address, order);
        order.setCustomer(user);
        order.setStatus(PENDING);

        BigDecimal totalAmount = cart.getCartItem().stream()
                .map(item -> item.getVariant().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal_amount(totalAmount);

        order.setOrderItems(cart.getCartItem().stream()
                .map(item -> OrderItem.builder()
                        .id(new OrderItemId(order.getId(), item.getVariant().getId()))
                        .order(order)
                        .variant(item.getVariant())
                        .quantity(item.getQuantity())
                        .price(item.getVariant().getPrice())
                        .build())
                .collect(Collectors.toSet()));

        cartRepository.delete(cart);
        return orderRepository.saveAndFlush(order).getId();
    }

    private void validateCart(Cart cart) {
        if (cart.getCartItem().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty, cannot proceed to checkout");
        }

        for (CartItem item : cart.getCartItem()) {
            if (item.getVariant().getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock for variant: " + item.getVariant().getId());
            }
        }
    }

    private Users getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    private Cart getCart(Users user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    private void addAddressToOrder(Address address, Order order) {
        order.setAddress(address.getAddress());
        order.setLatitude(address.getLatitude());
        order.setLongitude(address.getLongitude());
        order.setPhone_number(address.getPhone_number());
        order.setLandmark(address.getLandmark());
    }
}