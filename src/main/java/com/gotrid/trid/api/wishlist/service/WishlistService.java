package com.gotrid.trid.api.wishlist.service;

import com.gotrid.trid.api.product.dto.ProductResponse;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.core.product.mapper.ProductMapper;
import com.gotrid.trid.core.product.model.Product;
import com.gotrid.trid.core.product.repository.ProductRepository;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.user.repository.UserRepository;
import com.gotrid.trid.core.wishlist.model.Wishlist;
import com.gotrid.trid.core.wishlist.repository.WishlistRepository;
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
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productPage;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    @Transactional
    public void addOrRemoveFromWishlist(Integer productId, Integer userId) {
        Users user = getUser(userId);
        Product product = productPage.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        Optional<Wishlist> optionalWishlist = wishlistRepository.findByUser(user);

        Wishlist wishlist;
        if (optionalWishlist.isPresent()) {
            wishlist = optionalWishlist.get();
        } else {
            wishlist = new Wishlist();
            wishlist.setUser(user);
        }

        boolean exists = wishlist.getProducts().stream()
                .anyMatch(p -> p.equals(product));
        if (exists) {
            wishlist.getProducts().remove(product);
        } else {
            wishlist.getProducts().add(product);
        }

        wishlistRepository.save(wishlist);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getWishlist(int page, int size, Integer userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<Product> productPage = wishlistRepository.findWishlistProducts(userId, pageable);

        List<ProductResponse> responses = productPage.stream()
                .map(productMapper::toResponse)
                .toList();

        return new PageResponse<>(
                responses,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast()
        );
    }

    private Users getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
}
