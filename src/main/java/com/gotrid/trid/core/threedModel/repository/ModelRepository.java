package com.gotrid.trid.core.threedModel.repository;

import com.gotrid.trid.core.threedModel.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Integer> {
}
