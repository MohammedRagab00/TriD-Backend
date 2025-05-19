package com.gotrid.trid.core.photo.repository;

import com.gotrid.trid.core.photo.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}
