package com.gotrid.trid.infrastructure.service;

import com.gotrid.trid.common.exception.custom.UnAuthorizedException;
import com.gotrid.trid.api.shop.dto.CoordinateDTO;
import com.gotrid.trid.api.shop.dto.ModelDTO;
import com.gotrid.trid.core.shop.mapper.CoordinateMapper;
import com.gotrid.trid.core.shop.model.Coordinates;
import com.gotrid.trid.core.shop.model.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public abstract class BaseModelService {
    protected final CoordinateMapper coordinateMapper;

    protected void updateCoordinates(Coordinates coordinates, CoordinateDTO dto) {
        coordinates.setX_pos(dto.x_pos());
        coordinates.setY_pos(dto.y_pos());
        coordinates.setZ_pos(dto.z_pos());
        coordinates.setX_scale(dto.x_scale());
        coordinates.setY_scale(dto.y_scale());
        coordinates.setZ_scale(dto.z_scale());
        coordinates.setX_rot(dto.x_rot());
        coordinates.setY_rot(dto.y_rot());
        coordinates.setZ_rot(dto.z_rot());
    }

    protected Model getOrCreateModelAsset(Model existing) {
        return existing != null ? existing : new Model();
    }

    protected Coordinates getOrCreateCoordinates(Model model) {
        return model.getCoordinates() != null ? model.getCoordinates() : new Coordinates();
    }

    protected ModelDTO createModelDetails(Model model, String glbUrl) {
        CoordinateDTO coordinates = model != null && model.getCoordinates() != null ?
                coordinateMapper.toDTO(model.getCoordinates()) : null;
        return new ModelDTO(glbUrl, coordinates);
    }

    protected void validateOwnership(Integer ownerId, Integer actualOwnerId, String message) {
        if (!actualOwnerId.equals(ownerId)) {
            throw new UnAuthorizedException(message);
        }
    }
}
