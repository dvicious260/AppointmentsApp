package bowden.scheduling.DAO;

import bowden.scheduling.Helper.JDBC;
import bowden.scheduling.Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 The UserDaoImpl class provides methods for CRUD (create, read, update, delete) operations on Users table in the database.
 */
public class UserDaoImpl {

    /**
     * Inserts a new user into the Users table.
     *
     * @param user The User object to be inserted into the database.
     * @return True if the insertion was successful, false otherwise.
     * @throws SQLException If there was an error with the SQL query or database connection.
     */
    public static boolean insertUser(Users user) throws SQLException {
        String sql = "INSERT INTO users (User_ID, User_Name, Password) VALUES (?,?,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, user.getUserID());
        ps.setString(2, user.getUserName());
        ps.setString(3, user.getPassword());

        int rowsInserted = ps.executeUpdate();

        return rowsInserted > 0; // return true if the insert succeeded
    }

    /**
     * Updates an existing user in the Users table.
     *
     * @param user The User object to be updated in the database.
     * @return True if the update was successful, false otherwise.
     * @throws SQLException If there was an error with the SQL query or database connection.
     */
    public static boolean updateUser(Users user) throws SQLException {
        String sql = "UPDATE users SET User_Name = ?, Password = ?, Last_Update = NOW(), Last_Updated_By = USER() WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, user.getUserName());
        ps.setString(2, user.getPassword());
        ps.setInt(3, user.getUserID());

        int rowsUpdated = ps.executeUpdate();

        return rowsUpdated > 0;
    }

    /**
     * Retrieves a user from the Users table based on the user ID.
     *
     * @param userID The ID of the user to retrieve.
     * @return The User object representing the retrieved user, or null if no user was found with the given ID.
     * @throws SQLException If there was an error with the SQL query or database connection.
     */
    public static Users getUser(int userID) throws SQLException {
        Connection conn = JDBC.openConnection();
        String query = "SELECT * FROM users WHERE User_ID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            return new Users(userID, userName, password);
        }
        return null;
    }

    /**
     * Deletes a user from the Users table based on the user ID.
     *
     * @param userID The ID of the user to delete.
     * @return True if the deletion was successful, false otherwise.
     * @throws SQLException If there was an error with the SQL query or database connection.
     */
    public static boolean deleteUser(int userID) throws SQLException {
        String sql = "DELETE FROM users WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userID);
        int rowsDeleted = ps.executeUpdate();
        return rowsDeleted > 0;
    }

    /**
     * Retrieves all users from the Users table.
     *
     * @return An ObservableList of User objects representing all users in the database.
     */
    public static ObservableList<Users> getAllUsers() {
        ObservableList<Users> users = FXCollections.observableArrayList();

        try (Connection connection = JDBC.openConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("User_ID");
                String name = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");

                users.add(new Users(id, name, password));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return users;
    }
}
