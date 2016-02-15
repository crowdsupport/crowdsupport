package org.outofrange.crowdsupport.service;

import org.outofrange.crowdsupport.dto.FullUserDto;
import org.outofrange.crowdsupport.model.User;
import org.outofrange.crowdsupport.util.ServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    /**
     * Updates the user data of the currently authenticated user.
     *
     * @param userDto the new data to use
     * @return the updated user
     * @throws ServiceException if no user is currently authenticated, or non-admin users are trying to change their
     *                          username
     */
    User updateProfile(FullUserDto userDto);

    /**
     * Disables a user.
     *
     * @param userId the user id
     * @throws ServiceException if no user could be found
     */
    void disableUser(long userId);

    /**
     * Assigns the admin role to a user.
     *
     * @param userId the id of the user
     * @return the saved user
     * @throws ServiceException if no user could be found
     */
    User makeAdmin(long userId);

    /**
     * Creates a new user, hashes their password and sends a confirmation email if an address has been provided.
     *
     * @param userDto the user information to use
     * @return the newly created user
     * @throws ServiceException if there is already an existing, enabled user with the same username
     */
    User createUser(FullUserDto userDto);

    /**
     * Updates all user data of a user; not intended for normally privileged users.
     *
     * @param userId  the id of the user to update
     * @param userDto the information to use for the update
     * @return the updated user
     * @throws ServiceException if either no user could be found, or the username is already in use
     */
    User updateAll(long userId, FullUserDto userDto);

    /**
     * Loads all users having a username like the passed argument.
     *
     * @param like the text to look for
     * @return a list of all matching users
     */
    List<User> queryUsers(String like);

    /**
     * Loads a specific user
     *
     * @param id the id of the user
     * @return the loaded user
     */
    User loadUser(long id);

    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Returns the username of the currently authenticated user.
     *
     * @return the username of the currently authenticated user
     */
    Optional<String> getCurrentUsername();

    /**
     * Queries the database for an updated entity of the currently authenticated user.
     *
     * @return the currently authenticated user, or {@link Optional#empty()} of none could be found
     */
    Optional<User> getCurrentUserUpdated();

    /**
     * Loads all users.
     *
     * @return all users
     */
    List<User> loadAll();

    /**
     * Confirms the email address for whoever has the specified confirmation id.
     *
     * @param emailConfirmationId the confirmation id to use
     */
    void confirmEmail(String emailConfirmationId);

    /**
     * Sends a confirmation email to a user.
     *
     * @param user the user to send the email to
     */
    void sendConfirmationEmail(User user);
}
