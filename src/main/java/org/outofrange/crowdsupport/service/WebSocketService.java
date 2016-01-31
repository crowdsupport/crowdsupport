package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.dto.ChangeDto;
import org.outofrange.crowdsupport.dto.CommentDto;
import org.outofrange.crowdsupport.dto.DonationRequestDto;
import org.outofrange.crowdsupport.model.Place;

public interface WebSocketService {
    void sendChangeToPlace(ChangeDto<?> change, Place place);
}
