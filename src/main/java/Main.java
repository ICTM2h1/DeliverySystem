import System.Config.Config;
import System.Database.Query;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class provides the main entry point for the application.
 */
public class Main {

    /**
     * Main entry point of this class.
     *
     * @param args The given arguments.
     */
    public static void main(String[] args) {
        Config config = Config.getInstance();
        System.out.println("Application entry point.");

        ArrayList<String> selectFields = new ArrayList<>();
        selectFields.add("CityID");
        selectFields.add("CityName");

        HashMap<String, String> conditions = new HashMap<>();
        conditions.put("CityID", "1");
        conditions.put("CityName", "Aaronsburg");

        ArrayList<Object> results = Query.select("SELECT * FROM cities WHERE CityID = :CityID AND CityName = :CityName", selectFields, conditions);
        System.out.println("Select met condities " + results + "\n");

        HashMap<String, String> values = new HashMap<>();
        values.put("CityName", "Harderwijk");
        values.put("CityID", "123095");
        values.put("StateProvinceID", "1");
        values.put("LastEditedBy", "1");
        values.put("ValidFrom", "2013-01-01 00:00:00");
        values.put("ValidTo", "2021-01-01 00:00:00");

        boolean success = Query.insert("cities", values);
        System.out.println("Insert " + success + "\n");

        results = Query.select("SELECT * FROM cities ORDER BY CityID DESC LIMIT 2", selectFields);
        System.out.println("Select " + results + "\n");

        new Menu();

    }

}
