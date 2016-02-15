package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.model.DonationRequest;

import java.util.List;

public interface DonationRequestService {
    /**
     * Loads all donation requests.
     *
     * @return all donation requests.
     */
    List<DonationRequest> loadAll();

    /**
     * Resolves a donation request.
     *
     * @param id       the id of the donation request
     * @param resolved if the donation request should be resolved
     * @throws ServiceException if no donation request could be found
     */
    void setDonationRequestResolved(long id, boolean resolved);

    /**
     * Deletes a donation request. If no donation request has been found, this methos does nothing.
     *
     * @param id the id of the donation request that should be deleted
     */
    void deleteDonationRequest(long id);
}
