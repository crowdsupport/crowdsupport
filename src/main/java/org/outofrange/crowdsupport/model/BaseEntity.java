package org.outofrange.crowdsupport.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@MappedSuperclass
public class BaseEntity extends AbstractPersistable<Long> {
    @Version
    @Column(name = "version")
    private long version;

    public long getVersion() {
        return version;
    }
}
