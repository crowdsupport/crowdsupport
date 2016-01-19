package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findOneByName(String name);
}
