package org.outofrange.crowdsupport.dto;

import java.util.List;

public class StateWithCitiesDto extends StateDto {
    private List<CityDto> cities;

    public List<CityDto> getCities() {
        return cities;
    }

    public void setCities(List<CityDto> cities) {
        this.cities = cities;
    }
}
