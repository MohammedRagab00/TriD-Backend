package com.gotrid.trid.marshmello.models.Shops;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "shop")
@NoArgsConstructor
@AllArgsConstructor
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "shop_gltf")
    private String shopGltf;

    @Column(name = "shop_bin")
    private String shopBin;

    @Column(name = "shop_icon")
    private String shopIcon;

    @Column(name = "texture")
    private String texture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_shop_id", referencedColumnName = "id")
    private DetailsShop detailsShop;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinate_id", referencedColumnName = "id")
    private Coordinates coordinates;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Product> products;


    public Shop(Coordinates coordinates, DetailsShop detailsShop, String texture, String shopIcon, String shopBin, String shopGltf) {
        this.coordinates = coordinates;
        this.detailsShop = detailsShop;
        this.texture = texture;
        this.shopIcon = shopIcon;
        this.shopBin = shopBin;
        this.shopGltf = shopGltf;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", shopGltf='" + shopGltf + '\'' +
                ", shopBin='" + shopBin + '\'' +
                ", shopIcon='" + shopIcon + '\'' +
                ", texture='" + texture + '\'' +
                ", detailsShop=" + detailsShop +
                ", coordinates=" + coordinates +
                ", products=" + products +
                '}';
    }
}