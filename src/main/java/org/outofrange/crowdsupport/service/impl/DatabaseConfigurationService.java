package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.Property;
import org.outofrange.crowdsupport.persistence.PropertyRepository;
import org.outofrange.crowdsupport.service.ConfigurationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DatabaseConfigurationService implements ConfigurationService {
    private final PropertyRepository PropertyRepository;

    private final Map<String, Property> propertyCache = new HashMap<>();

    @Inject
    public DatabaseConfigurationService(PropertyRepository PropertyRepository) {
        this.PropertyRepository = PropertyRepository;
    }

    @Override
    public String getProperty(String key) {
        final Property cachedProperty = propertyCache.get(key);

        if (cachedProperty != null) {
            return cachedProperty.getValue();
        } else {
            final Property databaseProperty = PropertyRepository.findOneByKey(key);
            propertyCache.put(key, databaseProperty);

            return databaseProperty == null ? null : databaseProperty.getValue();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String setProperty(String key, String value) {
        Property property = PropertyRepository.findOneByKey(key);
        String previousValue = null;

        if (property == null) {
            property = new Property(key, value);
        } else {
            previousValue = property.getValue();
            property.setValue(value);
        }

        propertyCache.put(key, property);
        PropertyRepository.save(property);
        return previousValue;
    }

    @Override
    public void clearCache() {
        propertyCache.clear();
    }
}
