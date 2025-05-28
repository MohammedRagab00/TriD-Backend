package com.gotrid.trid.core.threedModel.mapper;

import com.gotrid.trid.core.threedModel.dto.CoordinateDTO;
import com.gotrid.trid.core.threedModel.model.Coordinates;
import org.springframework.stereotype.Component;

@Component
public class CoordinateMapper {

    public CoordinateDTO toDTO(Coordinates coordinate) {
        return new CoordinateDTO(
                coordinate.getX_pos(),
                coordinate.getY_pos(),
                coordinate.getZ_pos(),
                coordinate.getX_scale(),
                coordinate.getY_scale(),
                coordinate.getZ_scale(),
                coordinate.getX_rot(),
                coordinate.getY_rot(),
                coordinate.getZ_rot()
        );
    }

    public Coordinates toEntity(CoordinateDTO dto) {
        return Coordinates.builder()
                .x_pos(dto.x_pos())
                .y_pos(dto.y_pos())
                .z_pos(dto.z_pos())
                .x_scale(dto.x_scale())
                .y_scale(dto.y_scale())
                .z_scale(dto.z_scale())
                .x_rot(dto.x_rot())
                .y_rot(dto.y_rot())
                .z_rot(dto.z_rot())
                .build();
    }
}
