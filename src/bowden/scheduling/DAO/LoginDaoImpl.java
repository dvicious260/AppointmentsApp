package bowden.scheduling.DAO;

import bowden.scheduling.Helper.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 The LoginDaoImpl class provides methods for authenticating user login credentials against stored usernames and passwords in the database.
 */
public class LoginDaoImpl {

    /**
     * Authenticates the user's login credentials against stored usernames and passwords in the database.
     * @param username the username entered by the user
     * @param password the password entered by the user
     * @return true if the entered username and password match a stored username and password, false otherwise
     * @throws SQLException if an SQL exception occurs
     */
    public static boolean getLogin(String username, String password) throws SQLException {
        Connection conn = JDBC.openConnection();

        // Retrieve the stored username and password for the entered username
        String query = "SELECT * FROM users WHERE User_Name = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String storedPassword = rs.getString("Password");

            // Compare the retrieved password with the user's entered password
            if (password.equals(storedPassword)) {
                conn.close();
                return true;
            }
        }
        conn.close();
        return false;
    }

}