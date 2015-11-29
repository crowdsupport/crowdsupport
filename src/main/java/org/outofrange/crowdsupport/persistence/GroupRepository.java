package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
