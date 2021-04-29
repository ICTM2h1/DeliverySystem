package Authenthication;

/**
 * Provides a deliverer user.
 */
public class Deliverer extends User {

    /**
     * Instantiates a new deliverer.
     *
     * @param username of the deliverer.
     * @param password of the deliverer.
     * @param role of the deliverer.
     */
    public Deliverer(String username, String password, UserRole role) {
        super(username, password, role);
    }
}
