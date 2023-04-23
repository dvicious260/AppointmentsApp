package bowden.scheduling.Helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**

 The JDBC abstract class provides functionality to connect to a MySQL database and perform database operations.
 */
public abstract class JDBC {

    /**
     * The protocol used for the JDBC URL.
     */
    private static final String protocol = "jdbc";

    /**
     * The vendor for the JDBC URL.
     */
    private static final String vendor = ":mysql:";

    /**
     * The location for the JDBC URL.
     */
    private static final String location = "//localhost/";

    /**
     * The name of the database.
     */
    private static final String databaseName = "client_schedule";

    /**
     * The JDBC URL used to connect to the database.
     */
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL

    /**
     * The name of the JDBC driver.
     */
    private static final String driver = "com.mysql.cj.jdbc.Driver";

    /**
     * The username used to connect to the database.
     */
    private static final String userName = "sqlUser";

    /**
     * The password used to connect to the database.
     */
    private static final String password = "Passw0rd!";

    /**
     * A Connection object representing the database connection.
     */
    public static Connection connection;

    /**
     * Opens a connection to the database using the provided credentials.
     *
     * @return A Connection object representing the database connection.
     */
    public static Connection openConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}