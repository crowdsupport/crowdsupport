package org.outofrange.crowdsupport.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.nio.file.Path;

@Entity
@Table(name = "Users")
public class User extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private Path imagePath;
    private boolean admin;
    private boolean activated;
}
