package com.gotrid.trid.core.shop.model;

import com.gotrid.trid.common.model.AuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "model_asset")
public class Model extends AuditableEntity {

    private String glb;

    @OneToOne(cascade = CascadeType.ALL)
    private Coordinates coordinates;
}