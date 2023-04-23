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
 The ModifyAppointment class controls the logic for the modify appointment screen.
 This class allows the user to modify existing appointments and save the changes.
 The class is initialized when the user selects the modify button from the main screen.
 The class implements the Initializable interface to initialize the screen elements.
 The class also defines FXML fields for the various screen elements such as buttons, text fields, and combo boxes.
 */
public class ModifyAppointment implements Initializable {
    @FXML
    private TextField appointmentIDTextField;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<Contacts> contactComboBox;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private ComboBox<LocalTime> endComboBox;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField locationTextField;


    @FXML
    private ComboBox<LocalTime> startComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField typeTextField;

    @FXML
    private ComboBox<Users> userComboBox;

    /**
     This method cancels the current appointment and redirects the user to the home screen.
     @param event The ActionEvent that triggered this method.
     @throws IOException if there is an error loading the home screen.
     */
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        home(event);
    }

    /**
     This method saves the appointment information entered by the user and updates it in the database.
     If any required field is empty, it displays an error message.
     @param event The ActionEvent that triggered this method.
     @throws IOException if there is an error loading the home screen.
     @throws SQLException if there is an error updating the appointment in the database.
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
        int id = Integer.valueOf(appointmentIDTextField.getText());
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

        // Create appointment object and call DAO method to save it
        Appointments existingAppointment = AppointmentsDaoImpl.getAppointment(id);
        existingAppointment.setTitle(title);
        existingAppointment.setDescription(description);
        existingAppointment.setLocation(location);
        existingAppointment.setStart(start);
        existingAppointment.setEnd(end);
        existingAppointment.setCustomerID(customer.getId());
        existingAppointment.setUserID(user.getUserID());
        existingAppointment.setContactID(contact.getContactID());
        try {
            AppointmentsDaoImpl.updateAppointment(existingAppointment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        home(event);
    }

    /**
     This method populates the appointment information fields with the information
     from the given Appointments object.
     @param appointments The Appointments object to be displayed in the fields.
     @throws SQLException if there is an error retrieving the contact, customer, or user from the database.
     */
    @FXML
    public void sendAppointment(Appointments appointments) throws SQLException {
        appointmentIDTextField.setText(String.valueOf(appointments.getAppointmentID()));
        titleTextField.setText(appointments.getTitle());
        descriptionTextField.setText(appointments.getDescription());
        locationTextField.setText(appointments.getLocation());
        contactComboBox.setItems(ContactsDaoImpl.getAllContacts());
        //Get Contact by ID
        Contacts contact = ContactsDaoImpl.getContactById(appointments.getContactID());
        contactComboBox.setValue(contact);

        typeTextField.setText(appointments.getType());
        startComboBox.setItems(DateTime.getBusinessHours());
        endComboBox.setItems(DateTime.getBusinessHours());
        startDatePicker.setValue(appointments.getStart().toLocalDate());
        startComboBox.setValue(appointments.getStart().toLocalTime());
        endDatePicker.setValue(appointments.getEnd().toLocalDate());
        endComboBox.setValue(appointments.getEnd().toLocalTime());


        customerComboBox.setItems(CustomersDaoImpl.getAllCustomers());
        Customer customer = CustomersDaoImpl.getCustomer(appointments.getCustomerID());
        customerComboBox.setValue(customer);
        userComboBox.setItems(UserDaoImpl.getAllUsers());
        Users user = UserDaoImpl.getUser(appointments.getUserID());
        userComboBox.setValue(user);


    }

    /**
     Initializes the controller class.
     This method is called when the FXML loader loads the corresponding view.
     It disables the appointment ID text field to prevent the user from modifying it.
     @param url The URL of the FXML file.
     @param resourceBundle The ResourceBundle of the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIDTextField.setDisable(true);
    }
}
