package com.gotrid.trid.shop.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AssetInfo {
    private String gltf;
    private String bin;
    private String icon;
    private String texture;

    @OneToOne(cascade = CascadeType.ALL)
    private Coordinates coordinates;
}
