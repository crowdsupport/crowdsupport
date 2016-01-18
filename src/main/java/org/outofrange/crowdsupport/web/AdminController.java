package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.service.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

@RequestMapping(value = "/admin")
public class AdminController {
    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String showAdminPanel() {
        if (!userService.isUserLoggedIn()) {
            return "redirect:/";
        }

        return "admin/overview";
    }

    @RequestMapping(value = "/requestedPlaces", method = RequestMethod.GET)
    public String showOpenPageRequests(Model model) {
        if (!userService.isUserLoggedIn()) {
            return "redirect:/";
        }

        return "admin/openPlaceRequests";
    }
}
