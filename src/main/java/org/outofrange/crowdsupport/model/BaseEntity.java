package org.outofrange.crowdsupport.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.ZonedDateTime;

/**
 * Base class for all persisted entities.
 * <p>
 * Provides an ID, a version field for optimistic locking and a datetime, containing the timestamp when
 * the entity was created.
 */
@MappedSuperclass
public class BaseEntity extends AbstractPersistable<Long> {
    /**
     * The version of the persisted entity. Used for optimistic locking.
     * <p>
     * Starts at 0.
     */
    @Version
    @Column(name = "version")
    private long version;

    /**
     * Returns the current version of the entity.
     * <p>
     * A value of 0 means that the entity never has been updated in the database.
     *
     * @return the version of the entity.
     */
    public long getVersion() {
        return version;
    }

    /**
     * The datetime when the entity was constructed.
     */
    @Column(name = "created")
    private ZonedDateTime createdDateTime = ZonedDateTime.now();

    /**
     * Returns the {@link ZonedDateTime} the entity was constructed.
     * <p>
     * Note that this is <b>not</b> the timestamp it has been persisted for the first time!
     *
     * @return the {@code ZonedDateTime} of the initiation of the entity.
     */
    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }
}
