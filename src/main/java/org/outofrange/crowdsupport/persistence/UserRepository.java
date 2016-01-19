package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUsername(String username);

    List<User> findAllByUsernameContainingIgnoreCase(String username);
}
