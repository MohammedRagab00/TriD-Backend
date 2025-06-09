package com.gotrid.trid.core.cart.mapper;

import com.gotrid.trid.api.cart.dto.CartResponse;
import com.gotrid.trid.core.cart.model.CartItem;
import com.gotrid.trid.core.product.mapper.ProductVariantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class CartItemMapper implements Function<CartItem, CartResponse> {
    private final ProductVariantMapper variantMapper;

    @Override
    public CartResponse apply(CartItem cartItem) {
        return new CartResponse(
                cartItem.getVariant().getProduct().getId(),
                cartItem.getQuantity(),
                variantMapper.toResponse(cartItem.getVariant())
        );
    }
}
