package org.outofrange.crowdsupport.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.outofrange.crowdsupport.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.inject.Inject;

@Component
public class BaseController {
    @Inject
    private UserService userService;

    @Inject
    private ObjectMapper objectMapper;

    @ModelAttribute("currentUser")
    public String currentUserJson() {
        try {
            return objectMapper.writeValueAsString(userService.getCurrentUserDto());
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
