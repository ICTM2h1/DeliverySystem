package Authenthication;

/**
 * Provides an administrator user.
 */
public class Administrator extends User {

    /**
     * Instantiates a new admin.
     *
     * @param username of the admin.
     * @param password of the admin.
     * @param role of the admin.
     */
    public Administrator(String username, String password, UserRole role) {
        super(username, password, role);
    }
}
