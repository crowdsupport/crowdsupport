package org.outofrange.crowdsupport.rest.v2;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MappingController {
    private final ModelMapper mapper;

    public MappingController(ModelMapper mapper) {
        this.mapper = mapper;
    }

    protected ModelMapper getMapper() {
        return mapper;
    }

    protected <T> T map(Object source, Class<T> destination) {
        return mapper.map(source, destination);
    }

    protected <T> List<T> mapToList(Collection<?> source, Class<T> destination) {
        return mapCollection(source, destination).collect(Collectors.toList());
    }

    protected <T> Set<T> mapToSet(Collection<?> source, Class<T> destination) {
        return mapCollection(source, destination).collect(Collectors.toSet());
    }

    private <T> Stream<T> mapCollection(Collection<?> source, Class<T> destination) {
        return source.stream().map(i -> getMapper().map(i, destination));
    }
}
