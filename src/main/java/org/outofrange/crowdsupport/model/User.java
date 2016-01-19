package org.outofrange.crowdsupport.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Users")
public class User extends BaseEntity implements UserDetails {
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "imagepath")
    private String imagePath;

    @Column(name = "enabled")
    private boolean enabled = true;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UserRoles", joinColumns = {@JoinColumn(name = "user")}, inverseJoinColumns = {@JoinColumn(name = "role")})
    private Set<Role> roles = new HashSet<>();

    private boolean rehashPassword;

    public User() {
    }

    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> authorities = new HashSet<>();

        final Set<Role> roles = getRoles();
        roles.forEach(r -> authorities.addAll(r.getPermissions()));
        authorities.addAll(roles);

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        rehashPassword = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if ("".equals(email)) {
            email = null;
        }

        this.email = email;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean rehashPassword() {
        return rehashPassword;
    }

    public void setPasswordHash(String passwordHash) {
        this.password = passwordHash;
        rehashPassword = false;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        final Set<Role> roleSet;
        if (roles instanceof Set) {
            roleSet = (Set<Role>) roles;
        } else {
            roleSet = new HashSet<>(roles);
        }

        this.roles = roleSet;
    }
}
