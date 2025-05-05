package com.gotrid.trid.marshmello.dto.Shops;

import lombok.Data;

@Data
public class ShopRequest {
    private String shopGltf;
    private String shopBin;
    private String shopIcon;
    private String texture;
    private DetailsShopDTO detailsShop;
    private CoordinatesDTO coordinates;
}
