package org.outofrange.crowdsupport.rest.v2;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class TypeMappingController<T> extends MappingController {
    private final Class<T> mappingClass;

    public TypeMappingController(ModelMapper mapper, Class<T> mappingClass) {
        super(mapper);
        this.mappingClass = mappingClass;
    }

    protected List<T> mapToList(Collection<?> source) {
        return mapToList(source, mappingClass);
    }

    protected Set<T> mapToSet(Collection<?> source) {
        return mapToSet(source, mappingClass);
    }

    protected T map(Object object) {
        return getMapper().map(object, mappingClass);
    }
}
