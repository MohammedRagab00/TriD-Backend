package com.gotrid.trid.api.threedModel.controller;

import com.gotrid.trid.api.threedModel.dto.ModelResponse;
import com.gotrid.trid.api.threedModel.dto.ModelUpdateRequest;
import com.gotrid.trid.api.threedModel.service.IModelService;
import com.gotrid.trid.common.response.PageResponse;
import com.gotrid.trid.core.threedModel.dto.CoordinateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Model", description = "Model management APIs (Admin only)")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/models")
public class ModelController {

    private final IModelService modelService;

    @Operation(summary = "Create a new model", description = "Creates a new model (glb, images) and returns its ID",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE)
            ))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Shop created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Not a seller")
    })
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> uploadModelAssets(
            @RequestPart("glb") MultipartFile glbFile,
            @RequestPart(required = false, name = "images") List<MultipartFile> photoFiles
    ) {
        Integer modelId = modelService.createModel(glbFile, photoFiles);
        return ResponseEntity.created(URI.create("/api/v1/models/" + modelId)).body(modelId);
    }

    @Operation(summary = "Set model coordinates", description = "Sets the 3D coordinates of a model")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coordinates set successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Model not found")
    })
    @PutMapping("/{modelId}/coordinates")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> setShopCoordinates(
            @Parameter(description = "ID of the model") @PathVariable Integer modelId,
            @RequestBody CoordinateDTO coordinates) {
        modelService.updateCoordinates(modelId, coordinates);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Partially update model details", description = "Accepts partial updates including model asset, and images")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Model updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PatchMapping(value = "/{modelId}", consumes = MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> patchModel(
            @Parameter(description = "ID of the model") @PathVariable Integer modelId,
            @ModelAttribute ModelUpdateRequest modelUpdateRequest) {

        modelService.patchModel(modelId, modelUpdateRequest);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Get All models", description = "Retrieves all models")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Models retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Model not found")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<ModelResponse>> getAllModels(
            @Parameter(description = "Page number", schema = @Schema(defaultValue = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Items per page", schema = @Schema(defaultValue = "10"))
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(modelService.getAllModels(page, size));
    }

    @Operation(summary = "Get model", description = "Retrieves a specific model")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Model retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Model not found")
    })
    @GetMapping("/{modelId}")
    public ResponseEntity<ModelResponse> getModel(@PathVariable Integer modelId) {
        return ResponseEntity.ok(modelService.getModel(modelId));
    }

    @Operation(summary = "Delete model", description = "Deletes a model and all its associated assets")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Model deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Shop not found")
    })
    @DeleteMapping("/{modelId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteModel(
            @Parameter(description = "ID of the model") @PathVariable Integer modelId) {
        modelService.deleteShop(modelId);
        return ResponseEntity.noContent().build();
    }
}
