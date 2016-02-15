package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Loads a role identified by its {@code name}.
     * @param name the name of the role.
     * @return the found role, or {@link Optional#empty()} if none could be found.
     */
    Optional<Role> findOneByName(String name);
}
