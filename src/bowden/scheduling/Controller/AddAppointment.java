package bowden.scheduling.Controller;

import bowden.scheduling.DAO.AppointmentsDaoImpl;
import bowden.scheduling.DAO.ContactsDaoImpl;
import bowden.scheduling.DAO.CustomersDaoImpl;
import bowden.scheduling.DAO.UserDaoImpl;
import bowden.scheduling.Helper.DateTime;
import bowden.scheduling.Model.Appointments;
import bowden.scheduling.Model.Contacts;
import bowden.scheduling.Model.Customer;
import bowden.scheduling.Model.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static bowden.scheduling.Helper.Methods.home;

/**

 This class is responsible for adding a new appointment to the appointment database.

 It implements the Initializable interface to initialize the class.
 */
public class AddAppointment implements Initializable {

    @FXML
    private TextField appointmentID;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField typeTextField;

    @FXML
    private ComboBox<Contacts> contactComboBox;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private ComboBox<Users> userComboBox;

    @FXML
    private ComboBox<LocalTime> endComboBox;

    @FXML
    private ComboBox<LocalTime> startComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    /**

     This method cancels the addition of a new appointment and returns the user to the home screen.
     @param event the event triggered by the user clicking the cancel button
     @throws IOException if an error occurs during the loading of the home screen
     */
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        home(event);
    }
    /**

     This method saves the new appointment to the database, provided that all fields are filled in.

     @param event the event triggered by the user clicking the save button

     @throws IOException if an error occurs during the loading of the home screen

     @throws SQLException if an error occurs during the interaction with the appointment database
     */
    @FXML
    public void save(ActionEvent event) throws IOException, SQLException {
        if (titleTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty() || locationTextField.getText().isEmpty() || typeTextField.getText().isEmpty() || startDatePicker.getValue() == null || startComboBox.getValue() == null || endDatePicker.getValue() == null || endComboBox.getValue() == null || customerComboBox.getValue() == null || userComboBox.getValue() == null || contactComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Fields");
            alert.setHeaderText("There are blank fields");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }
        int id = AppointmentsDaoImpl.getMaxAppointmentID() + 1;
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = startComboBox.getValue();
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = endComboBox.getValue();
        LocalDateTime end = LocalDateTime.of(endDate, endTime);

// Get selected contact and user
        Contacts contact = contactComboBox.getValue();
        Users user = userComboBox.getValue();

// Get selected customer
        Customer customer = customerComboBox.getValue();

        Appointments newAppointment = new Appointments(id, title, description, location, type, start, end, customer.getId(), user.getUserID(), contact.getContactID());
        try {
            AppointmentsDaoImpl.insertAppointment(newAppointment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        home(event);
    }

    /**
     Initializes the appointment creation form by setting the maximum appointment ID, disabling the appointment ID field,
     and populating the contact, start time, end time, customer, and user combo boxes.
     @param url the location of the FXML file
     @param resourceBundle the resources used by the FXML file
     @throws RuntimeException if there is an error retrieving data from the database
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentID.setDisable(true);
        try {
            appointmentID.setText(String.valueOf(AppointmentsDaoImpl.getMaxAppointmentID() + 1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        contactComboBox.setItems(ContactsDaoImpl.getAllContacts());
        startComboBox.setItems(DateTime.getBusinessHours());
        endComboBox.setItems(DateTime.getBusinessHours());
        customerComboBox.setItems(CustomersDaoImpl.getAllCustomers());
        userComboBox.setItems(UserDaoImpl.getAllUsers());

    }
}
