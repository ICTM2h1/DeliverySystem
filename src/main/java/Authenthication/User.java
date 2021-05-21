package Authenthication;

/**
 * Provides a base class for users.
 */
public class User {

    private final String username;
    private final UserRole role;

    /**
     * Creates a new user object.
     *
     * @param username String username of user
     * @param role Integer user function role
     */
    public User(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }

    /**
     * Returns the username of the user.
     *
     * @return String username of user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the user role.
     *
     * @return The role.
     */
    public UserRole getRole() {
        return role;
    }

}
