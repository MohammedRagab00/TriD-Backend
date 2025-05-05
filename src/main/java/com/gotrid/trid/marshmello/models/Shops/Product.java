package com.gotrid.trid.marshmello.models.Shops;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private  Shop shop;

    @Column(name = "product_gltf")
    private String productGltf;

    @Column(name = "product_bin")
    private String productBin;

    @Column(name = "product_icon")
    private String productIcon;

    @Column(name = "texture")
    private String texture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_product_id", referencedColumnName = "id")
    private DetailsProduct detailsProduct;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinate_id", referencedColumnName = "id")
    private Coordinates coordinates;

    // Add all getters and setters
    // Add equals and hashCode
    // Add toString
}