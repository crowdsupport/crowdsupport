package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.model.Property;
import org.outofrange.crowdsupport.persistence.PropertyRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class DatabaseConfigurationServiceTest {
    private static final String KEY = "key";
    private static final String VALUE = "value";

    private Property defaultProperty;
    private PropertyRepository propertyRepository;

    private DatabaseConfigurationService databaseConfigurationService;

    @Before
    public void prepare() {
        defaultProperty = new Property(KEY, VALUE);
        propertyRepository = mock(PropertyRepository.class);

        databaseConfigurationService = new DatabaseConfigurationService(propertyRepository);
    }

    @Test
    public void gettingCachedProperty() {
        when(propertyRepository.findOneByKey(KEY)).thenReturn(defaultProperty);

        final String value = databaseConfigurationService.getProperty(KEY);

        assertEquals(VALUE, value);
        verify(propertyRepository).findOneByKey(KEY);

        verifyNoMoreInteractions(propertyRepository);
        final String cachedValue = databaseConfigurationService.getProperty(KEY);
        assertEquals(VALUE, cachedValue);
    }

    @Test
    public void gettingCachedPropertyAfterCacheClear() {
        when(propertyRepository.findOneByKey(KEY)).thenReturn(defaultProperty);

        final String value = databaseConfigurationService.getProperty(KEY);

        assertEquals(VALUE, value);
        verify(propertyRepository, times(1)).findOneByKey(KEY);

        databaseConfigurationService.clearCache();

        final String nonCachedValue = databaseConfigurationService.getProperty(KEY);
        assertEquals(VALUE, nonCachedValue);
        verify(propertyRepository, times(2)).findOneByKey(KEY);
    }

    @Test
    public void gettingNonExistingProperty() {
        when(propertyRepository.findOneByKey(KEY)).thenReturn(null);

        final String value = databaseConfigurationService.getProperty(KEY);

        assertNull(value);
        verify(propertyRepository).findOneByKey(KEY);
    }

    @Test
    public void gettingExistingProperty() {
        when(propertyRepository.findOneByKey(KEY)).thenReturn(defaultProperty);

        final String value = databaseConfigurationService.getProperty(KEY);

        assertEquals(VALUE, value);
        verify(propertyRepository).findOneByKey(KEY);
    }

    @Test
    public void settingNewProperty() {
        when(propertyRepository.findOneByKey(KEY)).thenReturn(null);

        final String oldValue = databaseConfigurationService.setProperty(KEY, VALUE);

        assertNull(oldValue);
        verify(propertyRepository).findOneByKey(KEY);
        verify(propertyRepository).save(any(Property.class));

        verifyNoMoreInteractions(propertyRepository);
        assertEquals(VALUE, databaseConfigurationService.getProperty(KEY));
    }

    @Test
    public void settingExistingProperty() {
        when(propertyRepository.findOneByKey(KEY)).thenReturn(defaultProperty);

        final String newValue = "newvalue";
        final String oldValue = databaseConfigurationService.setProperty(KEY, newValue);

        assertEquals(VALUE, oldValue);
        verify(propertyRepository).findOneByKey(KEY);
        verify(propertyRepository).save(any(Property.class));

        verifyNoMoreInteractions(propertyRepository);
        assertEquals(newValue, databaseConfigurationService.getProperty(KEY));
    }
}