package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.model.*;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.service.CommentService;
import org.outofrange.crowdsupport.util.Reflection;
import org.outofrange.crowdsupport.service.ServiceException;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class DonationRequestServiceImplTest {
    private DonationRequestRepository donationRequestRepository;
    private CommentService commentService;

    private DonationRequestServiceImpl donationRequestService;

    private final State state = new State("State name", "stateidentifier");
    private final City city = new City(state, "City name", "cityidentifier");
    private final Place place = new Place(city, "Place name", "placeidentifier", "Place location");
    private DonationRequest donationRequest;

    @Before
    public void prepare() {
        donationRequestRepository = mock(DonationRequestRepository.class);
        commentService = mock(CommentService.class);

        donationRequestService = new DonationRequestServiceImpl(donationRequestRepository, commentService);

        donationRequest = new DonationRequest(place, "Request title", "Request Description");
    }

    private Comment createCommentWithId(Long id) {
        final Comment comment = new Comment(donationRequest, new User("username", "password"), "Text");
        Reflection.setField(comment, "id", id);

        return comment;
    }

    @Test
    public void deletingKnownDonationRequestWithCommentsDeletesThoseToo() {
        final Comment comment = createCommentWithId(1L);
        donationRequest.addComment(comment);

        when(donationRequestRepository.findOne(1L)).thenReturn(donationRequest);

        donationRequestService.deleteDonationRequest(1);

        verify(donationRequestRepository).delete(donationRequest);
        verify(commentService).deleteComment(comment.getId());
    }

    @Test
    public void deletingKnownDonationRequestWorks() {
        when(donationRequestRepository.findOne(1L)).thenReturn(donationRequest);

        donationRequestService.deleteDonationRequest(1);

        verify(donationRequestRepository).delete(donationRequest);
        verifyZeroInteractions(commentService);
    }

    @Test
    public void deletingUnknownDonationRequestDoesNothing() {
        when(donationRequestRepository.findOne(1L)).thenReturn(null);

        donationRequestService.deleteDonationRequest(1);

        verify(donationRequestRepository, never()).delete(donationRequest);
        verifyZeroInteractions(commentService);
    }

    @Test
    public void loadAllReturnsNothing() {
        when(donationRequestRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(donationRequestService.loadAll().isEmpty());
    }

    @Test
    public void loadAllReturnsMultiple() {
        when(donationRequestRepository.findAll()).thenReturn(Collections.singletonList(donationRequest));

        assertThat(1, is(equalTo(donationRequestService.loadAll().size())));
    }

    @Test
    public void settingKnownRequestUnresovled() {
        testResolveRequest(false);
    }

    @Test
    public void settingKnownRequestResolved() {
        testResolveRequest(true);
    }

    private void testResolveRequest(boolean resolved) {
        when(donationRequestRepository.save(donationRequest)).thenReturn(donationRequest);
        when(donationRequestRepository.findOne(1L)).thenReturn(donationRequest);

        donationRequestService.setDonationRequestResolved(1, resolved);

        verify(donationRequestRepository).save(donationRequest);
        assertThat(resolved, is(equalTo(donationRequest.isResolved())));
    }

    @Test(expected = ServiceException.class)
    public void settingUnknownRequestResolvedThrowsException() {
        when(donationRequestRepository.findOne(1L)).thenReturn(null);

        donationRequestService.setDonationRequestResolved(1, true);
    }
}