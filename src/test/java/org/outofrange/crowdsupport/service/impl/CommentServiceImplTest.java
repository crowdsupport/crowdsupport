package org.outofrange.crowdsupport.service.impl;


import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.persistence.CommentRepository;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.service.UserService;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class CommentServiceImplTest {
    private CommentRepository commentRepository;
    private DonationRequestRepository donationRequestRepository;
    private UserService userService;

    private CommentServiceImpl commentService;

    @Before
    public void prepare() {
        commentRepository = mock(CommentRepository.class);
        donationRequestRepository = mock(DonationRequestRepository.class);
        userService = mock(UserService.class);

        commentService = new CommentServiceImpl(commentRepository, donationRequestRepository, userService);
    }

    @Test
    public void addingCommentToUnkownDonationRequest() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void addingNullToKnownDonationRequest() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void addingCommentToDonationRequest() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void deleteExistingComment() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void deleteNonExistingComment() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadAllReturnsNothing() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void loadAllReturnsMultiple() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void confirmUnkownComment() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void setConfirmedToTrue() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void setConfirmedToFalse() {
        throw new AssertionError("Not yet implemented");
    }
}