package org.outofrange.crowdsupport.service;

import java.util.List;

public interface CrowdsupportService<T> {
    T save(T entity);

    List<T> loadAll();
}
