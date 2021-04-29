package System.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Provides a statement class for converting named parameters to ?-marks inside the query.
 */
public class NamedParamStatement {

    private final PreparedStatement statement;
    private final ArrayList<String> fields = new ArrayList<>();

    /**
     * Creates a new named statement object.
     *
     * @param connection The connection.
     * @param query The sql query.
     */
    public NamedParamStatement(Connection connection, String query) throws SQLException {
        int position;
        while ((position = query.indexOf(":")) != -1) {
            int end = query.substring(position).indexOf(" ");
            if (end == -1) {
                end = query.length();
            } else {
                end += position;
            }

            String field = query.substring(position + 1, end);
            int comma = query.substring(position).indexOf(",");
            if (comma != -1) {
                field = field.substring(0, comma - 1);
            }

            this.fields.add(field);
            query = query.substring(0, position) + (comma != -1 ? "?," : "?") + query.substring(end);
        }

        this.statement = connection.prepareStatement(query);
    }

    /**
     * Executes the query.
     *
     * @return The result set.
     */
    public ResultSet executeQuery() throws SQLException {
        return this.statement.executeQuery();
    }

    /**
     * Executes the query.
     */
    public void execute() throws SQLException {
        this.statement.execute();
    }

    /**
     * Closes the connection with the database.
     */
    public void close() throws SQLException {
        this.statement.close();
    }

    /**
     * Returns the number of fields which have been converted to ?-marks inside the query.
     *
     * @return Number of fields.
     */
    public int fields() {
        return this.fields.size();
    }

    /**
     * Sets multiple values to the query.
     *
     * @param values The values to be used inside the query.
     */
    public void setValues(HashMap<String, String> values) throws SQLException {
        if (this.fields.size() < 1 || values == null || values.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<String, String>> it = values.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            this.setValue(String.valueOf(pair.getKey()), String.valueOf(pair.getValue()));
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    /**
     * Sets a value to the query.
     *
     * @param name The name of the named parameter.
     * @param value The value of the named parameter.
     */
    public void setValue(String name, String value) throws SQLException {
        if (this.fields.size() < 1) {
            return;
        }

        this.statement.setString(getIndex(name), value);
    }

    /**
     * Gets the index of the named parameter inside the found fields.
     *
     * @param name The name of the parameter.
     *
     * @return The index of the named parameter.
     */
    private int getIndex(String name) {
        if (this.fields.size() < 1 || !this.fields.contains(name)) {
            return 0;
        }

        return this.fields.indexOf(name) + 1;
    }

}
