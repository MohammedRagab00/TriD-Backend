package com.gotrid.trid.shop.mapper;

import com.gotrid.trid.shop.domain.ShopDetail;
import com.gotrid.trid.shop.dto.ShopDetailDTO;
import com.gotrid.trid.shop.dto.ShopDetailResponse;
import com.gotrid.trid.shop.dto.SocialDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public ShopDetailResponse toResponse(ShopDetail entity) {
        List<SocialDTO> socials = entity.getSocials().stream()
                .map(social -> new SocialDTO(social.getPlatform(), social.getLink()))
                .collect(Collectors.toList());

        return ShopDetailResponse.builder()
                .name(entity.getName())
                .category(entity.getCategory())
                .location(entity.getLocation())
                .description(entity.getDescription())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .socials(socials)
                .build();

    }

/*
    public ShopDetailDTO toDto(ShopDetail entity) {
        return ShopDetailDTO.builder()
                .name(entity.getName())
                .category(entity.getCategory())
                .location(entity.getLocation())
                .description(entity.getDescription())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .build();
    }
*/
}
