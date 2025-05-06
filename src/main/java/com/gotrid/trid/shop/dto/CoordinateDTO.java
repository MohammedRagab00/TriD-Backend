package com.gotrid.trid.shop.dto;

public record CoordinateDTO(
        double x_pos,
        double y_pos,
        double z_pos,
        double x_scale,
        double y_scale,
        double z_scale,
        double x_rot,
        double y_rot,
        double z_rot
) {
}
