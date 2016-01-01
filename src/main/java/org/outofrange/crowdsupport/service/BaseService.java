package org.outofrange.crowdsupport.service;

import java.util.List;

public interface BaseService<T> {
    T save(T entity);

    List<T> loadAll();
}
