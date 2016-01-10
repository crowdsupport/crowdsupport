package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.model.State;
import org.outofrange.crowdsupport.service.StateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.List;

@Controller
public class WelcomeController extends BaseController {
    @Inject
    private StateService stateService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showAllStates(Model model) {
        final List<State> states = stateService.loadAll();
        model.addAttribute("states", states);

        return "welcome";
    }
}
