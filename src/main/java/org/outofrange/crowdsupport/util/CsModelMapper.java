package org.outofrange.crowdsupport.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component("mapper")
public class CsModelMapper extends ModelMapper {
    public <T> List<T> mapToList(Collection<?> source, Class<T> destination) {
        return source.stream().map(i -> map(i, destination)).collect(Collectors.toList());
    }
}
