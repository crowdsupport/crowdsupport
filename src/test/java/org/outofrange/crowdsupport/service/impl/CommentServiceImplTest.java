package org.outofrange.crowdsupport.service.impl;


import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.model.*;
import org.outofrange.crowdsupport.persistence.CommentRepository;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.service.UserService;
import org.outofrange.crowdsupport.service.ServiceException;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CommentServiceImplTest {
    private CommentRepository commentRepository;
    private DonationRequestRepository donationRequestRepository;

    private CommentServiceImpl commentService;

    private final State state = new State("State name", "stateidentifier");
    private final City city = new City(state, "City name", "cityidentifier");
    private final Place place = new Place(city, "Place name", "placeidentifier", "Place location");
    private final DonationRequest donationRequest = new DonationRequest(place, "Request title", "Request Description");
    private final User user = new User("username", "password");

    private Comment comment;

    @Before
    public void prepare() {
        commentRepository = mock(CommentRepository.class);
        donationRequestRepository = mock(DonationRequestRepository.class);
        UserService userService = mock(UserService.class);

        commentService = new CommentServiceImpl(commentRepository, donationRequestRepository, userService);

        comment = new Comment(donationRequest, user, "Text");

        when(userService.getCurrentUserUpdated()).thenReturn(Optional.of(user));
    }

    @Test(expected = ServiceException.class)
    public void addingCommentToUnkownDonationRequestThrowsException() {
        when(donationRequestRepository.findOne(1L)).thenReturn(null);

        commentService.addComment(1, comment);
    }

    @Test(expected = NullPointerException.class)
    public void addingNullToKnownDonationRequestThrowsException() {
        commentService.addComment(1, null);
    }

    @Test
    public void addingCommentToDonationRequest() {
        when(commentRepository.save(comment)).thenReturn(comment);
        when(donationRequestRepository.findOne(1L)).thenReturn(donationRequest);

        commentService.addComment(1, comment);

        verify(commentRepository).save(comment);
    }

    @Test
    public void deleteExistingComment() {
        when(commentRepository.findOne(1L)).thenReturn(comment);

        commentService.deleteComment(1);

        verify(commentRepository).delete(comment);
    }

    @Test
    public void deleteNonExistingCommentDoesNothing() {
        when(commentRepository.findOne(1L)).thenReturn(null);

        commentService.deleteComment(1);

        verify(commentRepository, never()).delete(comment);
    }

    @Test
    public void loadAllReturnsNothing() {
        when(commentRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(commentService.loadAll().isEmpty());
    }

    @Test
    public void loadAllReturnsMultiple() {
        when(commentRepository.findAll()).thenReturn(Collections.singletonList(comment));

        assertThat(1, is(equalTo(commentService.loadAll().size())));
    }

    @Test(expected = ServiceException.class)
    public void confirmUnkownCommentThrowsException() {
        when(commentRepository.findOne(1L)).thenReturn(null);

        commentService.setCommentConfirmed(1, true);
    }

    @Test
    public void setConfirmedToTrue() {
        testConfirmed(true);
    }

    @Test
    public void setConfirmedToFalse() {
        testConfirmed(false);
    }

    private void testConfirmed(boolean confirmed) {
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentRepository.findOne(1L)).thenReturn(comment);

        commentService.setCommentConfirmed(1, confirmed);

        assertThat(confirmed, is(equalTo(comment.isConfirmed())));
        verify(commentRepository).save(comment);
    }
}