package org.outofrange.crowdsupport.rest;

import org.modelmapper.ModelMapper;
import org.outofrange.crowdsupport.dto.LinkBaseDto;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

abstract class TypedMappingController<T> extends MappingController {
    private final Class<T> mappingClass;

    public TypedMappingController(ModelMapper mapper, Class<T> mappingClass) {
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

    protected <S extends LinkBaseDto> ResponseEntity<S> created(S dto) {
        return ResponseEntity.created(dto.uri()).body(dto);
    }
}
