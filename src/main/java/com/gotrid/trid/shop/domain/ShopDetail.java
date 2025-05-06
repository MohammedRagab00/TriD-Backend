package com.gotrid.trid.shop.domain;

import com.gotrid.trid.infrastructure.common.AuditableEntity;
import com.gotrid.trid.shop.model.Social;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"socials"})
@Entity
public class ShopDetail extends AuditableEntity {
    @Column(unique = true, nullable = false, length = 40)
    private String name;
    @Column(length = 40)
    private String category;
    private String location;

    @Column(length = 1000, nullable = false)
    private String description;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Social> socials = new ArrayList<>();
}
