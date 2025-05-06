package com.gotrid.trid.shop.dto;

public record ProductDto(
        String name,
        String description,
        String gltfModel,
        String binModel,
        String textures,
        float xPos,
        float yPos,
        float zPos,
        float xScale,
        float yScale,
        float zScale,
        float xRot,
        float yRot,
        float zRot
) {
    public ProductDto(
            String name,
            String description,
            String gltfModel,
            String binModel,
            String textures
    ) {
        this(name, description, gltfModel, binModel, textures,
                0.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 0.0f
        );
    }
}
