package com.gotrid.trid.core.review.model;

import com.gotrid.trid.common.model.BaseEntity;
import com.gotrid.trid.core.product.model.Product;
import com.gotrid.trid.core.user.model.Users;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"user", "product"})
@Entity
public class Review extends BaseEntity {
    @ManyToOne(optional = false)
    private Users user;
    @ManyToOne(optional = false)
    private Product product;
    @Column(nullable = false)
    private Short rating;
    @Column(length = 200)
    private String comment;

    @PrePersist
    @PreUpdate
    private void validate() {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }
}
