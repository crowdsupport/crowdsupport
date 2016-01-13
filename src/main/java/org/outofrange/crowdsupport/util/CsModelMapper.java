package org.outofrange.crowdsupport.util;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CsModelMapper extends ModelMapper {
    public <T> List<T> mapToList(Collection<?> source, Class<T> destination) {
        return source.stream().map(i -> map(i, destination)).collect(Collectors.toList());
    }
}
