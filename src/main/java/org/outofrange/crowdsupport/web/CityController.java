package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.model.Place;
import org.outofrange.crowdsupport.service.CityService;
import org.outofrange.crowdsupport.service.PlaceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/support")
public class CityController extends BaseController {
    @Inject
    private PlaceService placeService;

    @Inject
    private CityService cityService;

    @RequestMapping(value = "/{stateIdentifier}/{cityIdentifier}", method = RequestMethod.GET)
    public String listPlaces(Model model, @PathVariable String stateIdentifier, @PathVariable String cityIdentifier) {
        final Optional<City> city = cityService.load(cityIdentifier);
        final List<Place> activePlaces = placeService.loadActivePlaces(cityIdentifier);

        if (city.isPresent() && city.get().getState().getIdentifier().equals(stateIdentifier)) {
            model.addAttribute("city", city.get());
            model.addAttribute("places", activePlaces);

            return "city/listPlaces";
        } else {
            return "redirect:/support/" + stateIdentifier;
        }
    }
}
