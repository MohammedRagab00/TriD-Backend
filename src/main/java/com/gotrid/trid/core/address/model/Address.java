package com.gotrid.trid.core.address.model;

import com.gotrid.trid.core.user.model.Users;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user"})
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    private Users user;
    @Column(length = 200)
    private String address;
    @Column(precision = 9, scale = 6, nullable = false)
    BigDecimal latitude;
    @Column(precision = 9, scale = 6, nullable = false)
    BigDecimal longitude;
    @Column(length = 10)
    private String phone_number;
    @Column(length = 100)
    private String landmark;
}