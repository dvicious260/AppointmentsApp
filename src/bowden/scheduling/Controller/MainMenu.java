package bowden.scheduling.Controller;

import bowden.scheduling.DAO.AppointmentsDaoImpl;
import bowden.scheduling.DAO.CustomersDaoImpl;
import bowden.scheduling.main.Main;
import bowden.scheduling.Model.Appointments;
import bowden.scheduling.Model.Customer;
import bowden.scheduling.Model.FirstLevelDivisions;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The MainMenu class represents the main menu screen of the scheduling application.
 * It allows the user to view, add, modify, and delete appointments and customers,
 * as well as generate reports.
 */
public class MainMenu implements Initializable {
    private static Customer customerSelection;
    private static Appointments appointmentSelection;
    @FXML
    private TableView<Appointments> appointmentTable;
    @FXML
    private TableColumn<Appointments, String> colAppContactID;

    @FXML
    private TableColumn<Appointments, Integer> colAppCustomerID;

    @FXML
    private TableColumn<Appointments, String> colAppDesc;

    @FXML
    private TableColumn<Appointments, LocalDateTime> colAppEnd;

    @FXML
    private TableColumn<Appointments, Integer> colAppID;

    @FXML
    private TableColumn<Appointments, String> colAppLoc;

    @FXML
    private TableColumn<Appointments, LocalDateTime> colAppStart;

    @FXML
    private TableColumn<Appointments, String> colAppTitle;

    @FXML
    private TableColumn<Appointments, String> colAppType;

    @FXML
    private TableColumn<Appointments, Integer> colAppUserID;
    @FXML
    private TableColumn<Customer, String> colCustomerAddress;
    @FXML
    private TableColumn<Customer, Integer> colCustomerID;

    @FXML
    private TableColumn<Customer, String> colCustomerName;

    @FXML
    private TableColumn<Customer, String> colCustomerPhone;

    @FXML
    private TableColumn<Customer, String> colCustomerPostal;

    @FXML
    private TableColumn<Customer, String> colCustomerState;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private Button btnLogout;
    @FXML
    private Button btnQuit;
    @FXML
    private Button btnAddAppointment;
    @FXML
    private Button btnAddCustomer;

    @FXML
    private Button btnDeleteAppointment;

    @FXML
    private Button btnDeleteCustomer;

    @FXML
    private Button btnModifyAppointment;

    @FXML
    private Button btnModifyCustomer;

    @FXML
    private Button buttonReports;

    @FXML
    private RadioButton rbuttonViewAll;

    @FXML
    private RadioButton rbuttonViewMonth;

    @FXML
    private RadioButton rbuttonViewWeek;

    /**
     * Loads the "addAppointment" FXML file into the scene and displays it in a new window.
     * @param event An ActionEvent representing the button click that triggered this method.
     * @throws IOException If there is an error loading the FXML file.
     */
    @FXML
    public void addAppointment(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/bowden/scheduling/View/addAppointment.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Calls the "loadAppointments" method with an empty filter to display all appointments.
     * @param event An ActionEvent representing the button click that triggered this method.
     */
    @FXML
    public void viewAll(ActionEvent event) {
        loadAppointments("");
    }

    /**
     * Calls the "loadAppointments" method with a filter to display appointments in the current month.
     * @param event An ActionEvent representing the button click that triggered this method.
     */
    @FXML
    public void viewMonth(ActionEvent event) {
        // Filter by current month
        int month = LocalDateTime.now().getMonthValue();
        String filter = "MONTH(start) = " + month;
        loadAppointments(filter);
    }

    /**
     * Calls the "loadAppointments" method with a filter to display appointments in the current week.
     * @param event An ActionEvent representing the button click that triggered this method.
     */
    @FXML
    public void viewWeek(ActionEvent event) {
        // Filter by current week
        LocalDateTime now = LocalDateTime.now();
        String filter = "WEEK(start) = WEEK('" + now + "')";
        loadAppointments(filter);
    }

    /**
     * Loads appointments from the database and displays them in the appointment table using the provided filter.
     * @param filter A SQL WHERE clause to filter the appointments to display.
     */
    private void loadAppointments(String filter) {
        AppointmentsDaoImpl appointmentsDao = new AppointmentsDaoImpl();
        appointmentTable.setItems(appointmentsDao.getAllAppointments(filter));
    }

    /**
     * Loads the "addCustomer" FXML file into the scene and displays it in a new window.
     * @param event An ActionEvent representing the button click that triggered this method.
     * @throws IOException If there is an error loading the FXML file.
     */
    @FXML
    public void addCustomer(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/bowden/scheduling/View/addCustomer.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Loads the "reports" FXML file into the scene and displays it in a new window.
     * @param event An ActionEvent representing the button click that triggered this method.
     * @throws IOException If there is an error loading the FXML file.
     */
    @FXML
    public void reports(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/bowden/scheduling/View/reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Deletes the selected appointment from the database.
     * @param event An ActionEvent representing the button click that triggered this method.
     * @throws SQLException If there is an error deleting the appointment from the database.
     */
    @FXML
    public void deleteAppointment(ActionEvent event) throws SQLException {
        appointmentSelection = appointmentTable.getSelectionModel().getSelectedItem();
        if (appointmentSelection == null) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.ERROR);
            noSelectionAlert.setTitle("No appointment selected");
            noSelectionAlert.setContentText("Please select a appointment to delete");
            noSelectionAlert.showAndWait();
        } else {
            Alert confirmAppointmentDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAppointmentDelete.setTitle("Delete appointment");
            confirmAppointmentDelete.setContentText("You are about to permanently delete this appointment. Are you sure you want to continue?");
            Optional<ButtonType> result = confirmAppointmentDelete.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AppointmentsDaoImpl.deleteAppointment(appointmentSelection.getAppointmentID());
                rbuttonViewAll.setSelected(true);
                appointmentTable.setItems(AppointmentsDaoImpl.getAllAppointments(""));
            }
        }

    }

    /**
     * Logs out the current user and returns to the login screen.
     * @param event An ActionEvent representing the button click that triggered this method.
     * @throws IOException If there is an error loading the login FXML file.
     */
    @FXML
    public void logout(ActionEvent event) throws IOException {
        Alert quitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        quitAlert.setTitle("Logout Application");
        quitAlert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> result = quitAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/bowden/scheduling/View/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Quits the application.
     * @param event An ActionEvent representing the button click that triggered this method.
     */
    @FXML
    public void quit(ActionEvent event) {
        Alert quitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        quitAlert.setTitle("Quit Application");
        quitAlert.setContentText("Are you sure you want to quit?");
        Optional<ButtonType> result = quitAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Deletes the selected customer from the database if the customer has no appointments.
     * @param event An ActionEvent representing the button click that triggered this method.
     * @throws SQLException If there is an error deleting the customer from the database.
     */
    @FXML
    public void deleteCustomer(ActionEvent event) throws SQLException {
        customerSelection = customerTable.getSelectionModel().getSelectedItem();
        if (customerSelection == null) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.ERROR);
            noSelectionAlert.setTitle("No customer selected");
            noSelectionAlert.setContentText("Please select a customer to delete");
            noSelectionAlert.showAndWait();
        } else {
            Alert confirmCustomerDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmCustomerDelete.setTitle("Delete Customer");
            confirmCustomerDelete.setContentText("You are about to permanently delete this customer. Are you sure you want to continue?");
            Optional<ButtonType> result = confirmCustomerDelete.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                CustomersDaoImpl.deleteCustomer(customerSelection.getId());
                customerTable.setItems(CustomersDaoImpl.getAllCustomers());
            }
        }

    }

    /**
     * Loads the "modifyAppointment" FXML file into the scene and displays it in a new window with the data from the selected appointment pre-populated.
     * @param event An ActionEvent representing the button click that triggered this method.
     * @throws IOException If there is an error loading the FXML file.
     * @throws SQLException If there is an error retrieving the appointment data from the database.
     */
    @FXML
    public void modifyAppointment(ActionEvent event) throws IOException, SQLException {
        appointmentSelection = appointmentTable.getSelectionModel().getSelectedItem();
        if (appointmentSelection == null) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.ERROR);
            noSelectionAlert.setTitle("No appointment selected");
            noSelectionAlert.setContentText("Please select a appointment to modify");
            noSelectionAlert.showAndWait();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/bowden/scheduling/View/modifyAppointment.fxml"));
            fxmlLoader.load();

            ModifyAppointment modifyAppointment = fxmlLoader.getController();
            modifyAppointment.sendAppointment(appointmentTable.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    /**
     * Loads the "modifyCustomer" FXML file into the scene and displays it in a new window with the data from the selected customer pre-populated.
     * @param event An ActionEvent representing the button click that triggered this method.
     * @throws IOException If there is an error loading the FXML file.
     * @throws SQLException If there is an error retrieving the appointment data from the database.
     */
    @FXML
    public void modifyCustomer(ActionEvent event) throws IOException, SQLException {
        customerSelection = customerTable.getSelectionModel().getSelectedItem();
        if (customerSelection == null) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.ERROR);
            noSelectionAlert.setTitle("No customer selected");
            noSelectionAlert.setContentText("Please select a customer to modify");
            noSelectionAlert.showAndWait();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/bowden/scheduling/View/modifyCustomer.fxml"));
            fxmlLoader.load();

            ModifyCustomer modifyCustomer = fxmlLoader.getController();
            modifyCustomer.sendCustomer(customerTable.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Initializes the controller class.
     * This method is automatically called by JavaFX after the FXML file has been loaded.
     * The method initializes the customer table and appointments table, and sets up event handlers
     * for user interactions.
     *
     * <p>The lambda expression in this method is used to set the cell value factory for the
     * "State/Province" column of the customer table. It retrieves the customer's division
     * and sets the value of the cell to the name of the division's corresponding state or province.
     * If an error occurs while retrieving the division from the database, a runtime exception is thrown.</p>
     *
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomersDaoImpl dao = new CustomersDaoImpl(); // Create an instance of the class
        customerTable.setItems(dao.getAllCustomers());
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCustomerPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        colCustomerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        // Use a lambda expression to set the cell value factory for the "State/Province" column
        colCustomerState.setCellValueFactory(cellData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            FirstLevelDivisions division = null;
            try {
                division = cellData.getValue().getDivision();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (division != null) {
                property.setValue(division.getDivisionName());
            } else {
                property.setValue("");
            }
            return property;
        });
        //Initialize appointments table
        //AppointmentsDaoImpl appDAO = new AppointmentsDaoImpl(); // Create an instance of the class
        rbuttonViewAll.setSelected(true);
        //appointmentTable.setItems(appDAO.getAllAppointments());
        colAppID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        colAppTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAppDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAppLoc.setCellValueFactory(new PropertyValueFactory<>("location"));
        colAppType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAppStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colAppEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colAppCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colAppUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colAppContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        loadAppointments("");


    }
}
