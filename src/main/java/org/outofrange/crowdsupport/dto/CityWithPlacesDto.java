package org.outofrange.crowdsupport.dto;

import java.util.List;

public class CityWithPlacesDto extends CityDto {
    private List<PlaceDto> places;

    public List<PlaceDto> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceDto> places) {
        this.places = places;
    }
}
