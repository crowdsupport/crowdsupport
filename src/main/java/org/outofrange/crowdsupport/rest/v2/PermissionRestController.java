package org.outofrange.crowdsupport.rest.v2;

import org.outofrange.crowdsupport.spring.ApiVersion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiVersion("2")
@RequestMapping(value = "/permission")
public class PermissionRestController {
}
