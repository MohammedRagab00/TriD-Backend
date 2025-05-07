package com.gotrid.trid.shop.model;

import com.gotrid.trid.infrastructure.common.AuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class ModelAsset extends AuditableEntity {

    private String gltf;
    private String bin;
    private String icon;
    private String texture;

    @OneToOne(cascade = CascadeType.ALL)
    private Coordinates coordinates;
}