package Authenthication;

import java.util.LinkedHashMap;

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

    },
    ADMIN (2) {

        /**
         * Changes function isAdmin to true in case enum status = 2.
         *
         * @return boolean value true if user is admin.
         */
        @Override
        public boolean isAdmin() {
            return true;
        }

    };

    private final int role;
    private static final LinkedHashMap<Integer, UserRole> roleMap = new LinkedHashMap<>();

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
    public static User createUser(String username, String password, UserRole userRole) {
        return new User(username, password, userRole);
    }
}