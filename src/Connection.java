import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

    private String hostname;
    private String port;
    private String username;
    private String password;
    private String database;
    private java.sql.Connection connection;

    /**
     * Default constructor.
     */
    public Connection() {
        this.hostname = "localhost";
        this.port = "3306";
        this.username = "root";
        this.password = "";
        this.database = "nerdygadgets";
    }

    /**
     * Create database connection.
     *
     * @param hostname host of the database
     * @param port port of the server
     * @param username username of the database
     * @param password password of the user
     * @param database name of database
     */
    public Connection(String hostname, String port, String username, String password, String database) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    /**
     * Creates connection to the database.
     */
    public void connect() {
        String url = "jdbc:mysql://"+this.hostname+":"+this.port+"/"+this.database;
        try {
            this.connection = DriverManager.getConnection(url, this.username, this.password);
            System.out.println("Succesfully connected to database: " + this.database);
        } catch(SQLException ex) {
            System.out.println(ex.getErrorCode());
            System.out.println("ddd");
            this.connection = null;
        }
    }

    /**
     * Returns hostname.
     *
     * @return hostname
     */
    public String getHostname() {
        return this.hostname;
    }

    /**
     * Sets a new hostname.
     *
     * @param hostname host of database
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Returns port.
     *
     * @return port of server
     */
    public String getPort() {
        return this.port;
    }

    /**
     * Sets a new port.
     *
     * @param port port of server
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Closes the connection to the database.
     */
    public void disconnect() {
        try {
            this.connection.close();
        } catch (SQLException s) {
            this.connection = null;
        }
    }

    /**
     * Returns username.
     *
     * @return username of the database
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets a new username.
     *
     * @param username username of the database
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns password.
     *
     * @return password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set a new password.
     *
     * @param password password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns name of the database.
     *
     * @return name of the database
     */
    public String getDatabase() {
        return this.database;
    }

    /**
     * Sets the name of the database.
     *
     * @param database name of the database
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Returns connection.
     *
     * @return connection to the database
     */
    public java.sql.Connection getConnection() {
        return this.connection;
    }

    /**
     * Checks if connection working.
     *
     * @return boolean for isConnected
     */
    public boolean isConnected() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException s) {
            return false;
        }
    }
}