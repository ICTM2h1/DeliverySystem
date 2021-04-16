package System.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NamedParamStatement {

    private final PreparedStatement prepStmt;
    private final ArrayList<String> fields = new ArrayList<>();

    public NamedParamStatement(Connection conn, String sql) throws SQLException {
        int pos;
        while ((pos = sql.indexOf(":")) != -1) {
            int end = sql.substring(pos).indexOf(" ");
            if (end == -1) {
                end = sql.length();
            } else {
                end += pos;
            }

            this.fields.add(sql.substring(pos + 1, end));
            sql = sql.substring(0, pos) + "?" + sql.substring(end);
        }

        this.prepStmt = conn.prepareStatement(sql);
    }

    public ResultSet executeQuery() throws SQLException {
        return this.prepStmt.executeQuery();
    }

    public void close() throws SQLException {
        this.prepStmt.close();
    }

    public int fields() {
        return this.fields.size();
    }

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

    public void setValue(String name, String value) throws SQLException {
        if (this.fields.size() < 1) {
            return;
        }

        this.prepStmt.setString(getIndex(name), value);
    }

    private int getIndex(String name) {
        if (this.fields.size() < 1) {
            return 0;
        }

        return this.fields.indexOf(name) + 1;
    }

}
