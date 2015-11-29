package org.outofrange.crowdsupport.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.nio.file.Path;

@Entity
@Table(name = "Cities")
public class City extends BaseEntity {
    private State state;
    private String name;
    private Path imagePath;
}
