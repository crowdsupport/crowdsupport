package org.outofrange.crowdsupport.rest.v2;

import org.outofrange.crowdsupport.service.CommentService;
import org.outofrange.crowdsupport.spring.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@ApiVersion("2")
@RequestMapping(value = "/comment")
public class CommentRestController {
    private static final Logger log = LoggerFactory.getLogger(CommentRestController.class);

    private final CommentService commentService;

    @Inject
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        log.info("Deleting comment with id {}", commentId);

        commentService.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }
}
