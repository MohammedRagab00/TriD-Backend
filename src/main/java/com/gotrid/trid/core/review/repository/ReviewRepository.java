package com.gotrid.trid.core.review.repository;

import com.gotrid.trid.core.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
