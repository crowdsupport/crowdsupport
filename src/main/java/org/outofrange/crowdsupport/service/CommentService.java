package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.Comment;
import org.outofrange.crowdsupport.persistence.CommentRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class CommentService {
    @Inject
    private CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}
