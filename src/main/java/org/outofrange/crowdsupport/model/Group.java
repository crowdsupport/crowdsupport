package org.outofrange.crowdsupport.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "Groups")
public class Group extends BaseEntity {
    private List<User> members;
}
