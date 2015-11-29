package org.outofrange.crowdsupport.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Comments")
public class Comment extends BaseEntity {
    private String text;
    private User author;
}
