package Crud;

import java.util.HashMap;

/**
 * Provides a class for interacting with the people table.
 */
public class People extends CrudBase {

    /**
     * Constructs a new people object.
     */
    public People() {
        super("people", "PersonID");

        this.addSelectField("LogonName");
        this.addSelectField("HashedPassword");
        this.addSelectField("FullName");
        this.addSelectField("Role");
    }

    /**
     * Gets a people by username.
     *
     * @param username The username.
     *
     * @return The fetched people.
     */
    public HashMap<String, String> getByUsername(String username) {
        this.addCondition("LogonName", username);

        return super.get("SELECT * FROM people WHERE LogonName = :LogonName");
    }

}
