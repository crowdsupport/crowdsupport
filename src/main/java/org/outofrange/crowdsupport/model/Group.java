package org.outofrange.crowdsupport.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Groups")
public class Group extends BaseEntity {
    @ManyToMany
    @JoinTable(name = "UsersGroups",
            joinColumns = {@JoinColumn(name = "group", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user", referencedColumnName = "id")})
    private List<User> members;
}
