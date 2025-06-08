package com.gotrid.trid.api.cart.service;

import com.gotrid.trid.api.product.dto.ProductVariantResponse;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.core.cart.model.Cart;
import com.gotrid.trid.core.cart.model.CartItem;
import com.gotrid.trid.core.cart.model.CartItemId;
import com.gotrid.trid.core.cart.repository.CartRepository;
import com.gotrid.trid.core.product.mapper.ProductVariantMapper;
import com.gotrid.trid.core.product.model.ProductVariant;
import com.gotrid.trid.core.product.repository.ProductVariantRepository;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductVariantMapper variantMapper;


    @Transactional
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
    public void removeFromCart(Integer variantId, Integer userId) {
        Users user = getUser(userId);
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new EntityNotFoundException("Product variant not found with id: " + variantId));

        Cart cart = cartRepository.findByUser(user).orElseThrow(
                () -> new EntityNotFoundException("Cart not found")
        );

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
    public PageResponse<ProductVariantResponse> getCart(int page, int size, Integer userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<ProductVariant> variantPage = cartRepository.findCartVariants(userId, pageable);

        List<ProductVariantResponse> responses = variantPage.stream()
                .map(variantMapper::toResponse)
                .toList();

        return new PageResponse<>(
                responses,
                variantPage.getNumber(),
                variantPage.getSize(),
                variantPage.getTotalElements(),
                variantPage.getTotalPages(),
                variantPage.isFirst(),
                variantPage.isLast()
        );

    }

    private Users getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
}
