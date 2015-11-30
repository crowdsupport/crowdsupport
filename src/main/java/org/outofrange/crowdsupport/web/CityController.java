package org.outofrange.crowdsupport.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/support")
public class CityController {
    @RequestMapping(value = "/{state}/{city}", method = RequestMethod.GET)
    public String listVenues(@PathVariable String state, @PathVariable String city) {
        return "city/listVenues";
    }
}
