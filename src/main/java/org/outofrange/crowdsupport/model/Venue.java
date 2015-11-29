package org.outofrange.crowdsupport.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.nio.file.Path;

@Entity
@Table(name = "Venues")
public class Venue extends BaseEntity {
    private City city;
    private String name;
    private Path imagePath;
}
