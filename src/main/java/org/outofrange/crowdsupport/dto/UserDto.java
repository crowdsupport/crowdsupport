package org.outofrange.crowdsupport.dto;

/**
 * @author Markus Möslinger
 */
public class UserDto {
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
}
