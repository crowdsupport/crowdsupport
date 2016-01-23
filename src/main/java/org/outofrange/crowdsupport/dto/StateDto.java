package org.outofrange.crowdsupport.dto;

public class StateDto extends LinkBaseDto {
    private String name;

    private String identifier;

    private String imagePath;

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    protected String self() {
        return "/state/" + getId();
    }
}
