package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.model.City;
import org.outofrange.crowdsupport.service.CityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;

@Controller
public class WelcomeController {
    @Inject
    private CityService cityService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showWelcomePage(Model model) {
        final List<City> cities = cityService.getRecentlyUsedCities();
        model.addAttribute("cities", cities);

        return "welcome";
    }
}
