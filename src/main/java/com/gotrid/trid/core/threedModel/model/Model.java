package com.gotrid.trid.core.threedModel.model;

import com.gotrid.trid.common.model.AuditableEntity;
import com.gotrid.trid.core.photo.model.Photo;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"coordinates", "photos"})
@Entity
@Table(name = "model_asset")
public class Model extends AuditableEntity {

    private String glb;

    @OneToOne(cascade = CascadeType.ALL)
    private Coordinates coordinates;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Photo> photos = new HashSet<>();
}