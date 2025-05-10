package com.gotrid.trid.user.repository;

import com.gotrid.trid.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String roleName);

    Set<Role> findByNameIn(Collection<String> names);
}
