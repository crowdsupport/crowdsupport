package org.outofrange.crowdsupport.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class BaseEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private long version;

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }
}
