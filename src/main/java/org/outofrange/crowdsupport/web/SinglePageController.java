package org.outofrange.crowdsupport.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SinglePageController extends BaseController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showApplication() {
        return "index";
    }
}
