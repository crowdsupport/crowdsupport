package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select sum(c.quantity) from Comment c")
    Long sumQuantity();
}
