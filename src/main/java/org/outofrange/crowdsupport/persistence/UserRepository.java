package org.outofrange.crowdsupport.persistence;

import org.outofrange.crowdsupport.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Loads the enabled user identified by its username.
     *
     * @param username the username of the user.
     * @return the found, enabled user, or {@link Optional#empty()} if none could be found.
     */
    Optional<User> findOneByUsernameAndEnabledTrue(String username);

    /**
     * Loads all users where their username matches {@code username}, ignoring case.
     *
     * @param username the text to look for in all usernames.
     * @return a list of all users having {@code username} in their names.
     */
    List<User> findAllByUsernameContainingIgnoreCase(String username);

    /**
     * Loads the user associated to a given email confirmation id.
     *
     * @param emailConfirmationId the email confirmation id.
     * @return the user with the given email confirmation id, or {@link Optional#empty()} if none could be found.
     */
    Optional<User> findOneByEmailConfirmationId(String emailConfirmationId);
}
