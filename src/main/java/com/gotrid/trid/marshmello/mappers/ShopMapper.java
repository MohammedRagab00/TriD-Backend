package com.gotrid.trid.marshmello.mappers;


import com.gotrid.trid.marshmello.dto.Shops.CoordinatesDTO;
import com.gotrid.trid.marshmello.dto.Shops.DetailsShopDTO;
import com.gotrid.trid.marshmello.models.Shops.Coordinates;
import com.gotrid.trid.marshmello.models.Shops.DetailsShop;


public class ShopMapper {

    public static Coordinates toEntity(CoordinatesDTO dto) {
        Coordinates coordinates = new Coordinates();
        coordinates.setXPos(dto.getXpos());
        coordinates.setYPos(dto.getYpos());
        coordinates.setZPos(dto.getZpos());
        coordinates.setXScale(dto.getXscale());
        coordinates.setYScale(dto.getYscale());
        coordinates.setZScale(dto.getZscale());
        coordinates.setXRot(dto.getXrot());
        coordinates.setYRot(dto.getYrot());
        coordinates.setZRot(dto.getZrot());
        return coordinates;
    }

    public static DetailsShop toEntity(DetailsShopDTO dto) {
        DetailsShop shop = new DetailsShop();
        shop.setName(dto.getName());
        shop.setType(dto.getType());
        shop.setLocation(dto.getLocation());
        shop.setDescription(dto.getDescription());
        shop.setEmail(dto.getEmail());
        shop.setPhone(dto.getPhone());
        shop.setFacebook(dto.getFacebook());
        return shop;
    }
}
