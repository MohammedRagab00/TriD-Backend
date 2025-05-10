package com.gotrid.trid.shop.service;

import com.gotrid.trid.exception.custom.UnAuthorizedException;
import com.gotrid.trid.shop.dto.AssetUrlsDTO;
import com.gotrid.trid.shop.dto.CoordinateDTO;
import com.gotrid.trid.shop.dto.ModelAssetsDTO;
import com.gotrid.trid.shop.mapper.CoordinateMapper;
import com.gotrid.trid.shop.model.Coordinates;
import com.gotrid.trid.shop.model.ModelAsset;
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

    protected ModelAsset getOrCreateModelAsset(ModelAsset existing) {
        return existing != null ? existing : new ModelAsset();
    }

    protected Coordinates getOrCreateCoordinates(ModelAsset modelAsset) {
        return modelAsset.getCoordinates() != null ? modelAsset.getCoordinates() : new Coordinates();
    }

    protected ModelAssetsDTO createAssetDetails(ModelAsset modelAsset, AssetUrlsDTO urls) {
        CoordinateDTO coordinates = modelAsset != null && modelAsset.getCoordinates() != null ?
                coordinateMapper.toDTO(modelAsset.getCoordinates()) : null;
        return new ModelAssetsDTO(urls, coordinates);
    }

    protected void validateOwnership(Integer ownerId, Integer actualOwnerId, String message) {
        if (!actualOwnerId.equals(ownerId)) {
            throw new UnAuthorizedException(message);
        }
    }
}
