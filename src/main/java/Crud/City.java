package Crud;

import java.util.LinkedHashMap;

/**
 * Provides a class for interacting with the city table.
 */
public class City extends CrudBase {

    /**
     * Constructs a new city object.
     */
    public City() {
        super("cities", "CityID");
    }

    /**
     * Gets a city by name.
     *
     * @param name The name.
     *
     * @return The city record.
     */
    public LinkedHashMap<String, String> getByName(String name) {
        String query = "SELECT * FROM cities WHERE CityName = :CityName LIMIT 1";
        this.addCondition("CityName", name);

        return super.get(query);
    }
}
