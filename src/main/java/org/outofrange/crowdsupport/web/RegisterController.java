package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/register")
public class RegisterController  {
    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String getRegisterForm() {
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doRegistration(User user, RedirectAttributes redirectAttributes) {
        userService.registerNewUser(user);

        redirectAttributes.addFlashAttribute("username", user.getUsername());

        return "redirect:/login";
    }
}
