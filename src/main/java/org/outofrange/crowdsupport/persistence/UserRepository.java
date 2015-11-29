package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
