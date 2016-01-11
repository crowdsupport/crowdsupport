package org.outofrange.crowdsupport.dto;

public class CityDto {
    private StateDto state;
    
    private String name;
    
    private String identifier;

    public StateDto getState() {
        return state;
    }

    public void setState(StateDto state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
