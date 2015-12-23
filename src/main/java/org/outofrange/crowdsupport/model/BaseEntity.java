package org.outofrange.crowdsupport.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.ZonedDateTime;

@MappedSuperclass
public class BaseEntity extends AbstractPersistable<Long> {
    @Version
    @Column(name = "version")
    private long version;

    public long getVersion() {
        return version;
    }

    @Column(name = "created")
    private ZonedDateTime createdDateTime = ZonedDateTime.now();

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }
}
