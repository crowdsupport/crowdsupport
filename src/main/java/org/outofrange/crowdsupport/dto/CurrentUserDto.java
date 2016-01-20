package org.outofrange.crowdsupport.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.outofrange.crowdsupport.model.Place;

import java.util.List;
import java.util.Set;

/**
 * @author Markus Möslinger
 */
public class CurrentUserDto extends UserDto {
    private String email;

    private String password;

    private Set<String> authorities;

    private Set<String> roles;

    private List<Place> managedPlaces;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public List<Place> getManagedPlaces() {
        return managedPlaces;
    }

    public void setManagedPlaces(List<Place> managedPlaces) {
        this.managedPlaces = managedPlaces;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", getUsername())
                .append("imagePath", getImagePath())
                .append("email", email)
                .append("authorities", authorities)
                .toString();
    }
}
