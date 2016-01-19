package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findOneByName(String name);
}
