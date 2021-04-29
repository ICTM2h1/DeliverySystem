package Authenthication;

import java.util.HashMap;

/**
 * Provides an enum with user roles.
 */
public enum UserRole {

    DELIVERER (1) {

        /**
         * Changes function isDeliverer to true in case enum stats = 1.
         *
         * @return boolean value true if user is deliverer.
         */
        @Override
        public boolean isDeliverer() {
            return true;
        }

        /**
         * Creates a user instance of logged in deliverer.
         *
         * @param username username of logged in deliverer.
         * @param password password of logged in deliverer.
         * @param userRole role of logged in deliverer.
         *
         * @return user instance of logged in deliverer.
         */
        @Override
        public Deliverer createUser(String username, String password, UserRole userRole) {
            return new Deliverer(username, password, userRole);
        }
    },
    ADMIN (2) {
        /**
         * changes function isAdmin to true in case enum status = 2.
         *
         * @return boolean value true if user is admin.
         */
        @Override
        public boolean isAdmin() {
            return true;
        }

        /**
         * Creates a user instance of logged in admin.
         *
         * @param username username of logged in admin.
         * @param password password of logged in admin.
         * @param userRole role of logged in admin.
         *
         * @return user instance of logged in admin.
         */
        @Override
        public Administrator createUser(String username, String password, UserRole userRole) {
            return new Administrator(username, password, userRole);
        }
    };

    private final int role;
    private static HashMap<Integer, UserRole> roleMap = new HashMap<>();

    /**
     * Constructs the user role.
     *
     * (1) -> Deliverer.
     * (2) -> Admin.
     *
     * @param role integer necessary to determine user role.
     */
    UserRole(int role) {
        this.role = role;
    }

    /* Create a static map with all defined user roles. */
    static {
        for (UserRole userRole : UserRole.values()) {
            roleMap.put(userRole.role, userRole);
        }
    }

    /**
     * Returns the ENUM-tag by it's given value.
     *
     * @param userRole integer necessary to identify admin/deliverer.
     * @return UserRole with corresponding value.
     */
    public static UserRole valueOf(int userRole) {
        return roleMap.get(userRole);
    }

    /**
     * Returns true if user has deliverer role.
     *
     * @return boolean value, true if user is deliverer, else false
     */
    public boolean isDeliverer() {
        return false;
    }

    /**
     * Returns true if user has admin role.
     *
     * @return boolean value, true if user is admin, else false
     */
    public boolean isAdmin() {
        return false;
    }

    /**
     * Creates a user instance of logged in user.
     *
     * @param username username of logged in user.
     * @param password password of logged in user.
     * @param userRole role of logged in user.
     *
     * @return user instance of logged in user.
     */
    public User createUser(String username, String password, UserRole userRole) {
        return new User(username, password, userRole);
    }
}