package com.gotrid.trid.core.user.repository;

import com.gotrid.trid.core.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository("jpa")
public interface RoleRepository extends IRoleRepository, JpaRepository<Role, Integer> {

    Optional<Role> findByName(String roleName);

    Set<Role> findByNameIn(Collection<String> names);
}
