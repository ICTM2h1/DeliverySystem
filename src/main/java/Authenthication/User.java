package Authenthication;

/**
 * Provides a base class for users.
 */
public class User {

    private final String username, password;
    private final UserRole role;

    /**
     * Creates a new user object.
     *
     * @param username String username of user
     * @param password String password of user
     * @param role Integer user function role
     */
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
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
