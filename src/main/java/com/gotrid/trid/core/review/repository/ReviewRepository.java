package com.gotrid.trid.core.review.repository;

import com.gotrid.trid.core.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProduct_Id(Integer productId);
}
