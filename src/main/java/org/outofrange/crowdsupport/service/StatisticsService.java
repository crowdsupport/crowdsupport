package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.dto.StatisticsDto;

public interface StatisticsService {
    /**
     * Queries the database for various statistics
     *
     * @return a DTO containing the statistics
     */
    StatisticsDto getStatistics();
}
