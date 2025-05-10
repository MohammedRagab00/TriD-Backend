package com.gotrid.trid.core.shop.model;

import com.gotrid.trid.common.model.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
public class Coordinates extends BaseEntity {
    private double x_pos;
    private double y_pos;
    private double z_pos;

    private double x_scale;
    private double y_scale;
    private double z_scale;

    private double x_rot;
    private double y_rot;
    private double z_rot;
}
