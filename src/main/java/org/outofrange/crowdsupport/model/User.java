package org.outofrange.crowdsupport.model;

import java.nio.file.Path;

public class User extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private Path imagePath;
    private boolean admin;
    private boolean activated;
}
