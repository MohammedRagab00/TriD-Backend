package com.gotrid.trid.api.threedModel.service;

import com.gotrid.trid.api.threedModel.dto.ModelResponse;
import com.gotrid.trid.api.threedModel.dto.ModelUpdateRequest;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.core.threedModel.dto.CoordinateDTO;
import com.gotrid.trid.core.threedModel.dto.ModelDTO;
import com.gotrid.trid.core.threedModel.mapper.CoordinateMapper;
import com.gotrid.trid.core.threedModel.model.Coordinates;
import com.gotrid.trid.core.threedModel.model.Model;
import com.gotrid.trid.core.threedModel.repository.ModelRepository;
import com.gotrid.trid.infrastructure.azure.ModelStorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
@Transactional
public class ModelService implements IModelService {

    private final ModelStorageService modelStorageService;
    private final ModelRepository modelRepository;
    private final CoordinateMapper coordinateMapper;

    @Override
    public Integer createModel(MultipartFile glbFile, List<MultipartFile> photos) {
        Model model = modelRepository.save(new Model());

        modelStorageService.uploadModelAsset(model, glbFile);

        if (photos != null) {
            modelStorageService.uploadModelPhotos(model, photos);
        }

        return model.getId();
    }

    @Override
    public void updateCoordinates(Integer modelId, CoordinateDTO coordinates) {
        Model model = findModelById(modelId);

        Coordinates updatedCoordinates = model.getCoordinates() != null ?
                model.getCoordinates()
                : new Coordinates();

        coordinateMapper.updateCoordinates(updatedCoordinates, coordinates);
        model.setCoordinates(updatedCoordinates);

        modelRepository.save(model);
    }

    @Override
    public void patchModel(Integer modelId, ModelUpdateRequest modelUpdateRequest) {
        Model model = findModelById(modelId);

        if (modelUpdateRequest.getGlb() != null) {
            modelStorageService.uploadModelAsset(model, modelUpdateRequest.getGlb());
        }
        if (modelUpdateRequest.getImages() != null && !modelUpdateRequest.getImages().isEmpty()) {
            modelStorageService.uploadModelPhotos(model, modelUpdateRequest.getImages());
        }
    }

    @Override
    public ModelResponse getModel(Integer modelId) {
        Model model = findModelById(modelId);

        return getModelResponse(model);
    }

    @Override
    public PageResponse<ModelResponse> getAllModels(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Model> modelPage = modelRepository.findAllCreatedByAdmins(pageable);

        List<ModelResponse> responses = modelPage.map(this::getModelResponse).getContent();

        return new PageResponse<>(
                responses,
                modelPage.getNumber(),
                modelPage.getSize(),
                modelPage.getTotalElements(),
                modelPage.getTotalPages(),
                modelPage.isFirst(),
                modelPage.isLast()
        );
    }

    @Override
    public void deleteShop(Integer modelId) {
        Model model = findModelById(modelId);

        modelStorageService.deleteModelAssets(model);
        modelRepository.delete(model);
    }

    private ModelResponse getModelResponse(Model model) {
        String glbUrl = modelStorageService.getUrl(model.getGlb());

        List<String> imagesUrls = model.getPhotos().stream()
                .map(photo -> modelStorageService.getUrl(photo.getUrl()))
                .toList();

        CoordinateDTO coordinates = model.getCoordinates() == null ? null :
                coordinateMapper.toDTO(model.getCoordinates());
        return new ModelResponse(
                new ModelDTO(glbUrl, coordinates),
                imagesUrls);
    }

    private Model findModelById(Integer modelId) {
        return modelRepository.findById(modelId)
                .orElseThrow(() -> new EntityNotFoundException("Model not found with ID: " + modelId));
    }
}
