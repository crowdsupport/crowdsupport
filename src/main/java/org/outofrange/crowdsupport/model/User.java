package org.outofrange.crowdsupport.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "Users")
public class User extends BaseEntity {
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "imagepath")
    private String imagePath;

    @Column(name = "admin")
    private boolean admin;

    @Column(name = "activated")
    private boolean activated;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;
}
