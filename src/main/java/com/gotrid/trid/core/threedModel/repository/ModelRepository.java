package com.gotrid.trid.core.threedModel.repository;

import com.gotrid.trid.core.threedModel.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ModelRepository extends JpaRepository<Model, Integer> {
    @Query("""
            SELECT m FROM Model m
            JOIN Users u ON m.createdBy = u.id
            JOIN u.roles r
            WHERE r.name = 'ROLE_ADMIN'
            """)
    Page<Model> findAllCreatedByAdmins(Pageable pageable);
}
