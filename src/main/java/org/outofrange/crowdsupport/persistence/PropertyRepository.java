package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    /**
     * Loads a property identified by a {@code key}.
     *
     * @param key the key of the property
     * @return the found property
     */
    Property findOneByKey(String key);
}
