package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

@Controller
@RequestMapping(value = "/request/newPlace")
public class PlaceRequestController extends BaseController {
    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String showPlaceRequestPage() {
        if (!userService.isUserLoggedIn()) {
            return "redirect:/";
        }

        return "placeRequest/show";
    }
}
