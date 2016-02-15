package org.outofrange.crowdsupport.rest;

import org.outofrange.crowdsupport.dto.StatisticsDto;
import org.outofrange.crowdsupport.service.StatisticsService;
import org.outofrange.crowdsupport.spring.api.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/statistics")
@ApiVersion("1")
public class StatisticsRestController {
    private static final Logger log = LoggerFactory.getLogger(StatisticsRestController.class);

    private final StatisticsService statisticsService;

    @Inject
    public StatisticsRestController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public StatisticsDto getStatistics() {
        log.info("Querying statistics");

        return statisticsService.getStatistics();
    }
}
