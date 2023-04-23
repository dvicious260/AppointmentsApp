package bowden.scheduling.DAO;

import bowden.scheduling.Helper.JDBC;
import bowden.scheduling.Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**

 The ContactsDaoImpl class represents the implementation of the data access object (DAO) for the Contacts model.
 It provides methods to retrieve a contact by ID and to retrieve all contacts.
 */
public class ContactsDaoImpl {

    /**
     * Retrieves the contact associated with the specified contact ID.
     * @param contactID the ID of the contact to retrieve
     * @return the Contacts object associated with the specified ID, or null if no such contact exists
     * @throws SQLException if an error occurs while attempting to retrieve the contact from the database
     */
    public static Contacts getContactById(int contactID) throws SQLException {
        Connection conn = JDBC.openConnection();
        String query = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, contactID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String contactName = rs.getString("Contact_Name");
            String email = rs.getString("Email");
            return new Contacts(contactID, contactName, email);
        }
        return null;
    }

    /**
     * Retrieves all contacts from the database.
     * @return an ObservableList containing all contacts
     */
    public static ObservableList<Contacts> getAllContacts() {
        ObservableList<Contacts> contacts = FXCollections.observableArrayList();

        try (Connection connection = JDBC.openConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("Contact_ID");
                String name = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");

                contacts.add(new Contacts(id, name, email));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return contacts;
    }

}