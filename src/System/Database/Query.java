package System.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides a class for interacting with the database.
 */
public class Query {

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
            System.out.println(e.getMessage());
            System.out.println("An error occurred while executing this query.");
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
            System.out.println(e.getMessage());
            System.out.println("An error occurred while executing this query.");
        }

        return null;
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
