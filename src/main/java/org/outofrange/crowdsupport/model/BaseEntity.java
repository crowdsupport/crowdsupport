package org.outofrange.crowdsupport.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class BaseEntity {
    @Id
    private Long id;

    @Version
    private long version;

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }
}
