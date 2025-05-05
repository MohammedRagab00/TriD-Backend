package com.gotrid.trid.marshmello.dto.Users;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String name;
    private String description;

    // Optional: This can be URLs if returning as a response
    private String gltfModelUrl;
    private String binModelUrl;

    private String[] textures;

    // Coordinates
    private float xPos = 0.0f;
    private float yPos = 0.0f;
    private float zPos = 0.0f;

    private float xScale = 1.0f;
    private float yScale = 1.0f;
    private float zScale = 1.0f;

    private float xRot = 0.0f;
    private float yRot = 0.0f;
    private float zRot = 0.0f;
}

