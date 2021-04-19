package System.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides a statement class for converting named parameters to ?-marks inside the query.
 */
public class NamedParamStatement {

    private final PreparedStatement prepStmt;
    private final ArrayList<String> fields = new ArrayList<>();

    /**
     * Creates a new named statement object.
     *
     * @param conn The connection.
     * @param sql The sql query.
     */
    public NamedParamStatement(Connection conn, String sql) throws SQLException {
        int position;
        while ((position = sql.indexOf(":")) != -1) {
            int end = sql.substring(position).indexOf(" ");
            if (end == -1) {
                end = sql.length();
            } else {
                end += position;
            }

            this.fields.add(sql.substring(position + 1, end));
            sql = sql.substring(0, position) + "?" + sql.substring(end);
        }

        this.prepStmt = conn.prepareStatement(sql);
    }

    /**
     * Executes the query.
     *
     * @return The result set.
     */
    public ResultSet executeQuery() throws SQLException {
        return this.prepStmt.executeQuery();
    }

    /**
     * Closes the connection with the database.
     */
    public void close() throws SQLException {
        this.prepStmt.close();
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
        if (this.fields.size() < 1) {
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

        this.prepStmt.setString(getIndex(name), value);
    }

    /**
     * Gets the index of the named parameter inside the found fields.
     *
     * @param name The name of the parameter.
     *
     * @return The index of the named parameter.
     */
    private int getIndex(String name) {
        if (this.fields.size() < 1) {
            return 0;
        }

        return this.fields.indexOf(name) + 1;
    }

}
