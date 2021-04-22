package Authenthication;

public class User {

    private String username;
    private String password;
    private int type;

    /**
     * Creates a new user object.
     *
     * @param username String username of user
     * @param password String password of user
     * @param type Integer user function id
     */
    public User(String username, String password, int type) {
        this.username = username;
        this.password = password;
        this.type = type;
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
     * Returns the function id of the user.
     *
     * @return Integer user function id
     */
    public int getType() {
        return this.type;
    }

    /**
     * Returns the name of the users function.
     *
     * @return String user function
     */
    public String getFunction() {
        return switch (this.type) {
            case 1 -> "Beheerder";
            case 2 -> "Bezorger";
            default -> null;
        };
    }
}
