package com.gotrid.trid.core.shop.mapper;

import com.gotrid.trid.api.shop.dto.CoordinateDTO;
import com.gotrid.trid.core.shop.model.Coordinates;
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
}
