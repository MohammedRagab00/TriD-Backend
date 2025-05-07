package com.gotrid.trid.shop.mapper;

import com.gotrid.trid.shop.domain.Shop;
import com.gotrid.trid.shop.dto.SocialDTO;
import com.gotrid.trid.shop.model.Social;
import org.springframework.stereotype.Component;

@Component
public class SocialMapper {

    public Social toEntity(SocialDTO dto, Shop shop) {
        return Social.builder()
                .platform(dto.platform())
                .link(dto.link())
                .shop(shop)
                .build();
    }

    public Social updateExisting(Social social, SocialDTO dto) {
        social.setPlatform(dto.platform());
        social.setLink(dto.link());
        return social;
    }

    public SocialDTO toDTO(Social social) {
        return new SocialDTO(
                social.getPlatform(),
                social.getLink()
        );
    }
}