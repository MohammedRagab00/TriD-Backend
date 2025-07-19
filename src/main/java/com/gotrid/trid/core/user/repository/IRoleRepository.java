    package com.gotrid.trid.core.user.repository;

    import com.gotrid.trid.core.user.model.Role;

    import java.util.Collection;
    import java.util.Optional;
    import java.util.Set;

    public interface IRoleRepository {
        Optional<Role> findByName(String roleName);

        Set<Role> findByNameIn(Collection<String> names);

        Role save(Role entity);
    }
