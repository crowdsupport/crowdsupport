package org.outofrange.crowdsupport.web;

import org.outofrange.crowdsupport.event.Events;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Since Crowdsupport is a single page application, there is only this controller providing a view directly.
 */
@Controller
public class SinglePageController {
    /**
     * Returns the index view and publishes an event that it has done that.
     * @return the index view.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showApplication() {
        Events.indexRequested().publish();

        return "index";
    }
}
