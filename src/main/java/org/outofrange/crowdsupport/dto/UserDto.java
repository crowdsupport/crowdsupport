package org.outofrange.crowdsupport.dto;

public class UserDto extends LinkBaseDto {
    private String username;

    private String imagePath;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    protected String self() {
        return "/user/" + getId();
    }
}
