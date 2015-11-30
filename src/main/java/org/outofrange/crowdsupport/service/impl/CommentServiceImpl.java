package org.outofrange.crowdsupport.service.impl;

import org.outofrange.crowdsupport.model.Comment;
import org.outofrange.crowdsupport.persistence.CommentRepository;
import org.outofrange.crowdsupport.service.CommentService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Inject
    private CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> loadAll() {
        return commentRepository.findAll();
    }
}
