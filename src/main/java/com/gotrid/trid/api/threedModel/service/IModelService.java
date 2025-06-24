package com.gotrid.trid.api.threedModel.service;

import com.gotrid.trid.api.threedModel.dto.ModelResponse;
import com.gotrid.trid.api.threedModel.dto.ModelUpdateRequest;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.core.threedModel.dto.CoordinateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IModelService {
    Integer createModel(MultipartFile glbFile, List<MultipartFile> photos);

    void updateCoordinates(Integer modelId, CoordinateDTO coordinates);

    void patchModel(Integer modelId, ModelUpdateRequest modelUpdateRequest);

    ModelResponse getModel(Integer modelId);

    PageResponse<ModelResponse> getAllModels(int page, int size);

    void deleteShop(Integer modelId);
}
