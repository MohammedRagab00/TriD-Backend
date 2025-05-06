package com.gotrid.trid.shop.mappers;

import com.gotrid.trid.shop.domain.ShopDetail;
import com.gotrid.trid.shop.dto.DetailsShopDTO;
import com.gotrid.trid.shop.model.Social;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShopDetailMapper {

    public ShopDetail toEntity(DetailsShopDTO dto) {
        ShopDetail shopDetail = ShopDetail.builder()
                .name(dto.name())
                .category(dto.category())
                .location(dto.location())
                .description(dto.description())
                .email(dto.email())
                .phone(dto.phone())
                .build();

        List<Social> socials = dto.socialLinks() != null ?
                dto.socialLinks().stream()
                        .map(s -> Social.builder()
                                .platform(s.platform())
                                .link(s.link())
                                .shop(shopDetail)
                                .build()
                        ).collect(Collectors.toList())
                : List.of();

        shopDetail.setSocials(socials);

        return shopDetail;
    }
}
