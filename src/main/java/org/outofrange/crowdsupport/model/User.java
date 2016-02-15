package org.outofrange.crowdsupport.model;

import org.outofrange.crowdsupport.util.RoleStore;
import org.outofrange.crowdsupport.util.Validate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This entity models the end user.
 */
@Entity
@Table(name = "USERS")
public class User extends BaseEntity implements UserDetails {
    /**
     * The role which is assigned to admins.
     */
    private static final Role ADMIN_ROLE = new Role(RoleStore.ADMIN);

    /**
     * The username of the user.
     */
    @Column(name = "username")
    private String username;

    /**
     * The password of the user. After persisting a newly set password, this field will store the password hash.
     */
    @Column(name = "password")
    private String password;

    /**
     * The email address of the user.
     */
    @Column(name = "email")
    private String email;

    /**
     * A random string of characters, indicating that the email address has to, and can be confirmed with it.
     */
    @Column(name = "emailconfirmation")
    private String emailConfirmationId;

    /**
     * The image path for the user.
     */
    @Column(name = "imagepath")
    private String imagePath;

    /**
     * A flag indicating if the user is enabled.
     */
    @Column(name = "enabled")
    private boolean enabled = true;

    /**
     * A list of comments the user is the author of.
     */
    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();

    /**
     * A set of roles assigned to the user.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USERS_ROLES", joinColumns = {@JoinColumn(name = "user")}, inverseJoinColumns = {@JoinColumn(name = "role")})
    private Set<Role> roles = new HashSet<>();

    /**
     * A list of teams the user is in.
     */
    @ManyToMany(mappedBy = "members")
    private List<Team> teams = new ArrayList<>();

    /**
     * A flag indicating if the value in {@link #password} needs to be hashed.
     */
    @Transient
    private boolean rehashPassword;

    protected User() { /* empty constructor for frameworks */ }

    /**
     * Creates a new user.
     *
     * @param username the username
     * @param password the raw password
     * @see #setUsername(String)
     * @see #setPassword(String)
     */
    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    /**
     * Returns the username of the user.
     *
     * @return the username of the user
     */
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

    /**
     * Returns if the user has the admin role.
     *
     * @return if the user has the admin role
     */
    public boolean isAdmin() {
        return getRoles().contains(ADMIN_ROLE);
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username of the user
     * @throws NullPointerException     when {@code username} is null
     * @throws IllegalArgumentException when {@code username} is empty
     */
    public void setUsername(String username) {
        Validate.notNullOrEmpty(username);

        this.username = username;
    }

    /**
     * Returns a set of authorities granted to the user.
     * <p>
     * This set will contain all {@link Role}s and their associated {@link Permission}s.
     *
     * @return a set of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> authorities = new HashSet<>();

        final Set<Role> roles = getRoles();
        roles.forEach(r -> authorities.addAll(r.getPermissions()));
        authorities.addAll(roles);

        return authorities;
    }

    /**
     * Returns the password of the user.
     * <p>
     * After persisting a newly set password, this method will return its hash.
     *
     * @return the password of the user
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Sets a raw password for the user, flagging it for hashing when persisting the user.
     *
     * @param password the raw password
     */
    public void setPassword(String password) {
        Validate.notNullOrEmpty(password);

        this.password = password;
        rehashPassword = true;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user. Setting an empty string will set the address to {@code null}.
     * <p>
     * If the email address is neither null nor empty, a random string will be assigned to {@code emailConfirmationId}.
     *
     * @param email the email address of the user
     */
    public void setEmail(String email) {
        if (email == null || "".equals(email)) {
            this.email = null;
        } else {
            this.email = email;
            emailConfirmationId = UUID.randomUUID().toString();
        }
    }

    /**
     * Returns true if an email confirmation id has been stored.
     *
     * @return true if an email confirmation id has been stored
     */
    public boolean hasEmailToBeConfirmed() {
        return emailConfirmationId != null;
    }

    /**
     * Returns the image path of the user.
     *
     * @return the image path of the user
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the image path of the user.
     *
     * @param imagePath the image path of the user
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Returns if the user is enabled.
     * <p>
     * Only enabled users are able to log in.
     *
     * @return if the user is enabled
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets if the user is enabled.
     *
     * @param enabled if the user is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns a list of comments the user is the author of.
     *
     * @return a list of comments the user is the author of
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Sets the list of comments the user is the author of.
     *
     * @param comments the list of comments the user is the author of
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Returns if the password retrieved by {@link #getPassword()} has to be hashed.
     *
     * @return if the password has to be hashed
     */
    public boolean rehashPassword() {
        return rehashPassword;
    }

    /**
     * Sets a hashed password.
     * <p>
     * After this call, {@link #rehashPassword()} will return {@code true}.
     *
     * @param passwordHash the hashed password
     * @throws NullPointerException     when {@code passwordHash} is null
     * @throws IllegalArgumentException when {@code passwordHash} is Empty
     */
    public void setPasswordHash(String passwordHash) {
        Validate.notNullOrEmpty(username);

        this.password = passwordHash;
        rehashPassword = false;
    }

    /**
     * Returns the set of roles assigned to the user.
     *
     * @return the set of roles assigned to the user
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Sets the roles assigned to the user
     * <p>
     * Duplicate roles will be removed.
     *
     * @param roles the roles assigned to the user
     */
    public void setRoles(Collection<Role> roles) {
        final Set<Role> roleSet;
        if (roles instanceof Set) {
            roleSet = (Set<Role>) roles;
        } else if (roles != null) {
            roleSet = new HashSet<>(roles);
        } else {
            roleSet = new HashSet<>();
        }

        this.roles = roleSet;
    }

    /**
     * Returns all places the user is in the managing team for.
     *
     * @return all places the user is in the managing team for
     */
    public List<Place> getManagedPlaces() {
        return teams.stream().map(Team::getPlace).collect(Collectors.toList());
    }

    /**
     * Returns the email confirmation id used to confirm the email address.
     * <p>
     * After confirming the email address, this method will return {@code null}.
     *
     * @return the email confirmation id
     */
    public String getEmailConfirmationId() {
        return emailConfirmationId;
    }

    /**
     * Sets the email confirmation id to null.
     * <p>
     * Should be done after confirming the email address.
     */
    public void setEmailConfirmationIdNull() {
        this.emailConfirmationId = null;
    }
}
