package org.outofrange.crowdsupport.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterController {
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String login() {
        return "register";
    }
}
