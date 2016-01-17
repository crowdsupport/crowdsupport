package org.outofrange.crowdsupport.dto;

import java.util.List;

public class PlaceWithDonationRequestsDto extends PlaceDto {
    private List<DonationRequestDto> donationRequests;

    public List<DonationRequestDto> getDonationRequests() {
        return donationRequests;
    }

    public void setDonationRequests(List<DonationRequestDto> donationRequests) {
        this.donationRequests = donationRequests;
    }
}
