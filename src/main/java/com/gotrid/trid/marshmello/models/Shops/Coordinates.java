package com.gotrid.trid.marshmello.models.Shops;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coordinates")
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long coordinatesId;
    @Column(name = "xPos")
    private double xPos;
    @Column(name = "yPos")
    private double yPos;
    @Column(name = "zPos")
    private double zPos;
    @Column(name = "xScale")
    private double xScale;
    @Column(name = "yScale")
    private double yScale;
    @Column(name = "zScale")
    private double zScale;
    @Column(name = "xRot")
    private double xRot;
    @Column(name = "yRot")
    private double yRot;
    @Column(name = "zRot")
    private double zRot;

    @Override
    public String toString() {
        return "Coordinates{" +
                "coordinatesId=" + coordinatesId +
                ", xPos=" + xPos +
                ", yPos=" + yPos +
                ", zPos=" + zPos +
                ", xScale=" + xScale +
                ", yScale=" + yScale +
                ", zScale=" + zScale +
                ", xRot=" + xRot +
                ", yRot=" + yRot +
                ", zRot=" + zRot +
                '}';
    }
// Getters and Setters
}
