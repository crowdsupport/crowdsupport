package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.service.CityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.Optional;

@Controller
@RequestMapping(value = "/support")
public class CityController {
    @Inject
    private CityService cityService;

    @RequestMapping(value = "/{stateIdentifier}/{cityIdentifier}", method = RequestMethod.GET)
    public String listPlaces(Model model, @PathVariable String stateIdentifier, @PathVariable String cityIdentifier) {
        final Optional<City> city = cityService.loadCity(cityIdentifier);

        if (city.isPresent() && city.get().getState().getIdentifier().equals(stateIdentifier)) {
            model.addAttribute("city", city.get());
            model.addAttribute("places", city.get().getPlaces());

            return "city/listVenues";
        } else {
            return "redirect:/support/" + stateIdentifier;
        }
    }
}
