package com.gotrid.trid.shop.mapper;

import com.gotrid.trid.shop.domain.ShopDetail;
import com.gotrid.trid.shop.dto.ShopDetailDTO;
import org.springframework.stereotype.Component;

@Component
public class ShopDetailMapper {
    public ShopDetail toEntity(ShopDetailDTO dto) {
        return ShopDetail.builder()
                .name(dto.name())
                .category(dto.category())
                .location(dto.location())
                .description(dto.description())
                .email(dto.email())
                .phone(dto.phone())
                .build();
    }
}
