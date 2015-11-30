package org.outofrange.crowdsupport.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/support")
public class StateController {
    @RequestMapping(value = "/{state}", method = RequestMethod.GET)
    public String listCities(@PathVariable String state) {
        return "state/listCities";
    }
}
