package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    /**
     * Loads a tag identified by its name.
     *
     * @param name the name of the tag.
     * @return the found tag, or {@link Optional#empty()} if none could be found.
     */
    Optional<Tag> findOneByName(String name);

    /**
     * Loads all tags where {@code namePart} is part of the name.
     *
     * @param namePart the text to look for.
     * @return a list of tags with {@code namePart} in their names.
     */
    List<Tag> findAllByNameContainingIgnoreCase(String namePart);
}
