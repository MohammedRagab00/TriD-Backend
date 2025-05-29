package com.gotrid.trid.infrastructure.service;

import com.gotrid.trid.common.exception.custom.UnAuthorizedException;
import com.gotrid.trid.core.threedModel.dto.CoordinateDTO;
import com.gotrid.trid.core.threedModel.dto.ModelDTO;
import com.gotrid.trid.core.threedModel.mapper.CoordinateMapper;
import com.gotrid.trid.core.threedModel.model.Coordinates;
import com.gotrid.trid.core.threedModel.model.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public abstract class BaseModelService {
    protected final CoordinateMapper coordinateMapper;

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
