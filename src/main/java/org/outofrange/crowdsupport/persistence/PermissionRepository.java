package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    /**
     * Loads a permission by its name.
     *
     * @param name the name of the permission.
     * @return the found permission, or {@link Optional#empty()} if none could be found.
     */
    Optional<Permission> findOneByName(String name);
}
