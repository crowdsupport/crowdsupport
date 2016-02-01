package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findOneByName(String name);

    List<Tag> findAllByNameContainingIgnoreCase(String namePart);
}
