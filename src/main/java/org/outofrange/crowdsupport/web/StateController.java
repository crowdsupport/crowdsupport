package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.service.StateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.Optional;

@Controller
@RequestMapping(value = "/support")
public class StateController {
    @Inject
    private StateService stateService;

    @RequestMapping(value = "/{stateidentifier}", method = RequestMethod.GET)
    public String listCities(Model model, @PathVariable String stateidentifier) {
        Optional<State> loadedState = stateService.load(stateidentifier);

        if (loadedState.isPresent()) {
            model.addAttribute("state", loadedState.get());
            model.addAttribute("cities", loadedState.get().getCities());

            return "state/listCities";
        } else {
            return "redirect:/";
        }
    }
}
