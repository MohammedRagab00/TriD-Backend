package com.gotrid.trid.marshmello.models.Shops;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name = "details_product")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailsProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "sizes")
    private String sizes;

    @Column(name = "colors")
    private String colors;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "count")
    private Integer count;

    @Column(name = "price")
    private Double price;

}
