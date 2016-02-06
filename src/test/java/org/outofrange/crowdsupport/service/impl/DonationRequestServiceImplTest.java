package org.outofrange.crowdsupport.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.outofrange.crowdsupport.persistence.DonationRequestRepository;
import org.outofrange.crowdsupport.service.CommentService;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class DonationRequestServiceImplTest {
    private DonationRequestRepository donationRequestRepository;
    private CommentService commentService;

    private DonationRequestServiceImpl donationRequestService;

    @Before
    public void prepare() {
        donationRequestRepository = mock(DonationRequestRepository.class);
        commentService = mock(CommentService.class);

        donationRequestService = new DonationRequestServiceImpl(donationRequestRepository, commentService);
    }

    @Test
    public void deletingKnownDonationRequestWithCommentsDeletesThoseToo() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void deletingKnownDonationRequestWorks() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void deletingUnkownDonationRequestReturnsNothing() {
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
    public void settingKnownRequestUnresovled() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void settingKnownRequestResolved() {
        throw new AssertionError("Not yet implemented");
    }

    @Test
    public void settingUnkownRequestResolvedThrowsException() {
        throw new AssertionError("Not yet implemented");
    }
}