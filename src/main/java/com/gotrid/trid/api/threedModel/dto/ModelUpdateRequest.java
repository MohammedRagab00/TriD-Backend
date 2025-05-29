package com.gotrid.trid.api.threedModel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Schema(description = "DTO for partial update of a model. Files (glbModel, photos) are optional.")
public class ModelUpdateRequest {
    @Schema(description = "Updated 3D GLB model file")
    private MultipartFile glb;

    @Schema(description = "Updated shop photos")
    private List<MultipartFile> images;
}
