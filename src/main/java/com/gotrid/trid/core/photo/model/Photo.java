package com.gotrid.trid.core.photo.model;

import com.gotrid.trid.common.model.AuditableEntity;
import com.gotrid.trid.core.shop.model.Shop;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"shop"})
@Entity
public class Photo extends AuditableEntity {

    @Column(length = 100, nullable = false)
    private String url;

    @ManyToOne
    @OnDelete(action = CASCADE)
    private Shop shop;
}
