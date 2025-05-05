package com.gotrid.trid.marshmello.dto.Shops;

import lombok.Getter;
import lombok.Setter;

// DTO for Coordinates
@Getter
@Setter
public class CoordinatesDTO {
    private double xpos;
    private double ypos;
    private double zpos;
    private double xscale;
    private double yscale;
    private double zscale;
    private double xrot;
    private double yrot;
    private double zrot;

    @Override
    public String toString() {
        return "CoordinatesDTO{" +
                "xpos=" + xpos +
                ", ypos=" + ypos +
                ", zpos=" + zpos +
                ", xscale=" + xscale +
                ", yscale=" + yscale +
                ", zscale=" + zscale +
                ", xrot=" + xrot +
                ", yrot=" + yrot +
                ", zrot=" + zrot +
                '}';
    }

// Getters and setters for each field
}


