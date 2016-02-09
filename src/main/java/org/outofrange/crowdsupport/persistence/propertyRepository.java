package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    Property findOneByKey(String key);
}
