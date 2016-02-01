package org.outofrange.crowdsupport.dto;

public class TagDto extends LinkBaseDto {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected String self() {
        return "/tag/" + getId();
    }
}
