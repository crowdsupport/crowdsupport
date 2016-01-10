package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController extends BaseController {
    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String showProfile(Model model) {
        final User user = userService.getCurrentUserUpdated().get();

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile/show";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String editProfile(Model model, RedirectAttributes redirectAttributes, @RequestParam String email, @RequestParam String password) {
        final User user = userService.getCurrentUserUpdated().get();

        user.setEmail(email);

        if (!"".equals(password)) {
            user.setPassword(password);
        }

        userService.save(user);

        redirectAttributes.addFlashAttribute("message", "Profile changed successfully");
        return "redirect:/profile";
    }
}
