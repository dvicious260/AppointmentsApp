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
 The ModifyCustomer class is the controller for the Modify Customer form.
 It allows the user to edit an existing customer's information and save the changes.
 The class includes methods for populating the form with the customer's current information,
 saving changes to the database, and canceling the modification process.
 The class implements the Initializable interface to initialize the form when it is loaded.
 */
public class ModifyCustomer implements Initializable {
    @FXML
    private TextField modifyCustomerAddress;

    @FXML
    private Button modifyCustomerCancel;

    @FXML
    private ComboBox<Countries> modifyCustomerCountry;

    @FXML
    private TextField modifyCustomerID;

    @FXML
    private TextField modifyCustomerPostal;

    @FXML
    private TextField modifyCustomerName;

    @FXML
    private TextField modifyCustomerPhone;

    @FXML
    private Button modifyCustomerSave;

    @FXML
    private ComboBox<FirstLevelDivisions> modifyCustomerState;

    /**
     * The cancel method cancels the modification process and returns to the home screen.
     *
     * @param event An ActionEvent that is triggered when the "Cancel" button is clicked.
     * @throws IOException if there is an error navigating back to the home screen.
     */
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        home(event);
    }

    /**
     * The saveCustomer method saves the changes made to an existing customer to the database.
     * If any required fields are blank, an error message is displayed and the method returns.
     *
     * @param event An ActionEvent that is triggered when the "Save" button is clicked.
     * @throws IOException if there is an error navigating back to the home screen.
     * @throws SQLException if there is an error updating the customer in the database.
     */
    @FXML
    public void saveCustomer(ActionEvent event) throws IOException, SQLException {
        if (modifyCustomerName.getText().isEmpty() || modifyCustomerAddress.getText().isEmpty() || modifyCustomerPhone.getText().isEmpty() || modifyCustomerPostal.getText().isEmpty() || modifyCustomerState.getValue() == null || modifyCustomerCountry.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Fields");
            alert.setHeaderText("There are blank fields");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }
        String name = modifyCustomerName.getText();
        int id = Integer.parseInt(modifyCustomerID.getText());
        String address = modifyCustomerAddress.getText();
        String postal = modifyCustomerPostal.getText();
        String phone = modifyCustomerPhone.getText();
        int divisionId = modifyCustomerState.getSelectionModel().getSelectedItem().getDivisionID();

        // Get the existing customer record from the database
        Customer existingCustomer = CustomersDaoImpl.getCustomer(id);
        existingCustomer.setName(name);
        existingCustomer.setAddress(address);
        existingCustomer.setPostalCode(postal);
        existingCustomer.setPhone(phone);
        existingCustomer.setDivisionID(divisionId);

        // Update the existing customer record with the new information
        try {
            CustomersDaoImpl.updateCustomer(existingCustomer);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Navigate back to the home screen
        home(event);
    }

    /**
     Sets the fields of the modify customer form with the information of the given customer,
     populates the country and division combo boxes, and sets the selected country and division.
     @param customer the customer whose information will be displayed in the modify customer form
     @throws SQLException if there is an error retrieving data from the database
     The lambda function is used to listen for changes in the country combo box. When a new country is selected,
     the function populates the divisions combo box based on the selected country, and selects the division of
     the given customer if it exists in the new country. If there is no division for the customer in the new country,
     the function selects the first division in the list.
     */
    @FXML
    public void sendCustomer(Customer customer) throws SQLException {
        modifyCustomerName.setText(customer.getName());
        modifyCustomerID.setText(String.valueOf(customer.getId()));
        modifyCustomerAddress.setText(customer.getAddress());
        modifyCustomerPhone.setText(customer.getPhone());
        modifyCustomerPostal.setText(customer.getPostalCode());

        // populate the country combo box
        ObservableList<Countries> countries = CountriesDaoImpl.getAllCountries();
        modifyCustomerCountry.setItems(countries);

        // set the selected country
        modifyCustomerCountry.setValue(customer.getCountry());

        // set the selected country using a lambda function to listen for changes in the combo box
        modifyCustomerCountry.getSelectionModel().selectedItemProperty().addListener((observableValue, oldCountry, newCountry) -> {
            // Populate the divisions combo box based on the selected country
            ObservableList<FirstLevelDivisions> divisions = DivisionsDaoImpl.getDivisionsByCountryId(newCountry.getCountryID());
            modifyCustomerState.setItems(divisions);

            // If the customer has a division in the new country, select it in the divisions combo box
            try {
                if (customer.getCountry().equals(newCountry) && customer.getDivision() != null && divisions.contains(customer.getDivision())) {
                    modifyCustomerState.setValue(customer.getDivision());
                } else {
                    modifyCustomerState.setValue(divisions.get(0));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        // populate the division combo box based on the selected country
        ObservableList<FirstLevelDivisions> divisions = DivisionsDaoImpl.getDivisionsByCountryId(customer.getCountry().getCountryID());
        modifyCustomerState.setItems(divisions);

        // set the selected division
        modifyCustomerState.getSelectionModel().select(customer.getDivision());
    }

    /**
     Disables the modifyCustomerID field on the modify customer form.
     Called when the form is initialized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyCustomerID.setDisable(true);
    }
}
