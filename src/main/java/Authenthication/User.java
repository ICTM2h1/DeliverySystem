package Authenthication;

public class User {

    private String username;
    private String password;
    private UserRole role;

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
}
