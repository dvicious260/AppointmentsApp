package bowden.scheduling.Controller;

import bowden.scheduling.DAO.CountriesDaoImpl;
import bowden.scheduling.DAO.CustomersDaoImpl;
import bowden.scheduling.DAO.DivisionsDaoImpl;
import bowden.scheduling.Model.Countries;
import bowden.scheduling.Model.Customer;
import bowden.scheduling.Model.FirstLevelDivisions;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static bowden.scheduling.Helper.Methods.home;


/**
 * A controller class for adding a new customer.
 */
public class AddCustomer implements Initializable {

    @FXML
    private TextField newCustomerAddress;

    @FXML
    private TextField newCustomerPostal;

    @FXML
    private ComboBox<Countries> newCustomerCountry;

    @FXML
    private TextField newCustomerID;

    @FXML
    private TextField newCustomerName;

    @FXML
    private TextField newCustomerPhone;

    @FXML
    private ComboBox<FirstLevelDivisions> newCustomerState;

    @FXML
    private Button newCustomerCancel;

    @FXML
    private Button newCustomerSave;

    /**
     * Handles the cancel button action and navigates back to the home screen.
     *
     * @param event The event triggered by the user's action.
     * @throws IOException If there is an error navigating to the home screen.
     */
    @FXML
    public void cancelAdd(ActionEvent event) throws IOException {
        home(event);
    }

    /**
     * Saves the new customer information and navigates back to the home screen.
     *
     * @param event The event triggered by the user's action.
     * @throws SQLException If there is an error inserting the customer into the database.
     * @throws IOException  If there is an error navigating to the home screen.
     */
    @FXML
    public void saveCustomer(ActionEvent event) throws SQLException, IOException {
        if (newCustomerName.getText().isEmpty() || newCustomerPhone.getText().isEmpty() || newCustomerAddress.getText().isEmpty() || newCustomerPostal.getText().isEmpty() || newCustomerState.getValue() == null || newCustomerCountry.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Fields");
            alert.setHeaderText("There are blank fields");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        int id = CustomersDaoImpl.getMaxCustomerId() + 1;
        String customerName = newCustomerName.getText();
        String customerPhone = newCustomerPhone.getText();
        String customerAddress = newCustomerAddress.getText();
        String customerPostal = newCustomerPostal.getText();
        FirstLevelDivisions division = newCustomerState.getSelectionModel().getSelectedItem();
        int divisionID = division.getDivisionID();

        Customer newCustomer = new Customer(id, customerName, customerAddress, customerPostal, customerPhone, divisionID, division);
        try {
            CustomersDaoImpl.insertCustomer(newCustomer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        home(event);
    }

    /**
     * Deletes the selected customer.
     *
     * @param event The event triggered by the user's action.
     * @throws IOException If there is an error navigating to the home screen.
     */
    @FXML
    public void deleteCustomer(ActionEvent event) throws IOException {

    }
    /**
     * Initializes the add customer form.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newCustomerID.setDisable(true);
        try {
            newCustomerID.setText(String.valueOf(CustomersDaoImpl.getMaxCustomerId() + 1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
// populate the country combo box
        ObservableList<Countries> countries = CountriesDaoImpl.getAllCountries();
        newCustomerCountry.setItems(countries);

        // set up a listener for the country combo box to populate the division combo box
        newCustomerCountry.getSelectionModel().selectedItemProperty().addListener((observableValue, oldCountry, newCountry) -> {
            ObservableList<FirstLevelDivisions> divisions = DivisionsDaoImpl.getDivisionsByCountryId(newCountry.getCountryID());
            newCustomerState.setItems(divisions);
        });
    }
}
