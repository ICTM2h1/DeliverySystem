package System.Database;

import System.Config.Config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides a class for interacting with the database.
 */
public class Query {

    private static Config config = Config.getInstance();

    /**
     * The connection object.
     */
    private static Connection connection;

    /**
     * Creates a new database instance.
     */
    private Query() {
        try {
            connection = new Connection();
        } catch(Exception e) {
            System.out.println("An error occurred while connecting to the database.");
        }
    }

    /**
     * Instantiates an instance for the database or gets an existing one.
     *
     * @return The database instance.
     */
    private static Query getInstance() {
        return new Query();
    }

    /**
     * Selects data from the database.
     *
     * @param query The query to be executed.
     * @param selectFields The fields to be selected from the returned data.
     *
     * @return An array list with the data of the result set.
     */
    public static ArrayList<Object> select(String query, ArrayList<String> selectFields) {
        try {
            getInstance();

            Statement stmt = connection.get().createStatement();
            ArrayList<Object> results = resultSetToArray(selectFields, stmt.executeQuery(query));
            connection.close();

            return results;
        } catch(Exception e) {
            System.out.println("An error occurred while executing this query.");
            if (Boolean.parseBoolean(config.get("debug"))) {
                System.out.println(e.getMessage());
            }
        }

        return null;
    }

    /**
     * Selects data from the database.
     *
     * @param query The query to be executed.
     * @param selectFields The fields to be selected from the returned data.
     * @param conditions The conditions of the query.
     *
     * @return An array list with the data of the result set.
     */
    public static ArrayList<Object> select(String query, ArrayList<String> selectFields, HashMap<String, String> conditions) {
        try {
            getInstance();

            NamedParamStatement stmt = new NamedParamStatement(connection.get(), query);
            if (stmt.fields() == 0 && conditions.size() > 0) {
                throw new RuntimeException("All specified conditions must be used inside the query.");
            }

            stmt.setValues(conditions);
            ArrayList<Object> results = resultSetToArray(selectFields, stmt.executeQuery());
            stmt.close();
            connection.close();

            return results;
        } catch(Exception e) {
            System.out.println("An error occurred while executing this query.");
            if (Boolean.parseBoolean(config.get("debug"))) {
                System.out.println(e.getMessage());
            }
        }

        return null;
    }

    /**
     * Inserts data into a table inside the database.
     *
     * @param table The table to insert data in.
     * @param values The data to be inserted into the database.
     *
     * @return Whether the data was successfully inserted or not.
     */
    public static boolean insert(String table, HashMap<String, String> values) {
        boolean success;
        try {
            getInstance();

            HashMap<String, String> insertValues = (HashMap<String, String>) values.clone();
            StringBuilder queryColumns = new StringBuilder();
            StringBuilder queryValues = new StringBuilder();
            Iterator<Map.Entry<String, String>> it = values.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = it.next();

                queryColumns.append(pair.getKey()).append(it.hasNext() ? ", " : " ");
                queryValues.append(":").append(pair.getKey()).append(it.hasNext() ? ", " : " ");

                it.remove(); // avoids a ConcurrentModificationException
            }

            String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table, queryColumns, queryValues);
            NamedParamStatement stmt = new NamedParamStatement(connection.get(), query, insertValues.size());
            if (stmt.fields() == 0 && insertValues.size() > 0) {
                throw new RuntimeException("All specified conditions must be used inside the query.");
            }

            stmt.setValues(insertValues);
            stmt.execute();
            stmt.close();
            connection.close();

            success = true;
        } catch(Exception e) {
            success = false;
            System.out.println("An error occurred while executing this query.");
            if (Boolean.parseBoolean(config.get("debug"))) {
                System.out.println(e.getMessage());
            }
        }

        return success;
    }

    /**
     * Renders the result to array.
     *
     * @param selectFields The fields to be selected from the returned data.
     * @param rs The result set.
     *
     * @return An array list with the data of the result set.
     */
    private static ArrayList<Object> resultSetToArray(ArrayList<String> selectFields, ResultSet rs) throws SQLException {
        ArrayList<Object> results = new ArrayList<>();
        while (rs.next()) {
            HashMap<String, String> selectedValues = new HashMap<>();
            for (String field : selectFields) {
                selectedValues.put(field, rs.getString(field));
            }

            results.add(selectedValues);
        }

        return results;
    }

}
