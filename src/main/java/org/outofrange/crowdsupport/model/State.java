package org.outofrange.crowdsupport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.nio.file.Path;

@Entity
@Table(name = "States")
public class State extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "imagepath")
    private Path imagePath;
}
