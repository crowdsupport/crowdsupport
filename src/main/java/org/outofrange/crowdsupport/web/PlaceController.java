package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.service.PlaceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.Optional;

@Controller
@RequestMapping(value = "/support/{stateIdentifier}/{cityIdentifier}/{placeIdentifier}")
public class PlaceController {
    @Inject
    private PlaceService placeService;

    @RequestMapping(method = RequestMethod.GET)
    public String showAllRequests(@PathVariable String stateIdentifier, @PathVariable String cityIdentifier,
                                  @PathVariable String placeIdentifier, Model model) {
        Optional<Place> place = placeService.load(stateIdentifier, cityIdentifier, placeIdentifier);
        final Place p = place.get();

        if (place.isPresent()) {
            model.addAttribute("place", p);

            return "place/listRequests";
        } else {
            return "redirect:/support/" + stateIdentifier + "/" + cityIdentifier;
        }
    }
}
