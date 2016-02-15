package org.outofrange.crowdsupport.automation.scenarios;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.outofrange.crowdsupport.automation.keyword.ui.PlaceKeywords;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.model.User;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class PlaceUiTest extends UiTest {
    private Place place;
    private User user;

    @Before
    public void prepare() {
        user = data().user().createUser(true);
        place = data().place().getPlace();
    }

    @Test
    public void createDonationRequest() {
        keywords().ui().sidePanel().login(user).doWait();

        keywords().ui().dashboard().browseStates()
                .gotoCities(place.getCity().getState().getIdentifier())
                .gotoPlaces(place.getCity().getIdentifier())
                .gotoPlace(place.getIdentifier());
    }
}
