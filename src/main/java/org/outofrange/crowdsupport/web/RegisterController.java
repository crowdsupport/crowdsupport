package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;

@RequestMapping(value = "/register")
public class RegisterController extends BaseController {
    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String getRegisterForm() {
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doRegistration(User user, RedirectAttributes redirectAttributes) {
        userService.save(user);

        redirectAttributes.addFlashAttribute("username", user.getUsername());

        return "redirect:/login";
    }
}
