package bowden.scheduling.Controller;

import bowden.scheduling.DAO.AppointmentsDaoImpl;
import bowden.scheduling.DAO.LoginDaoImpl;
import bowden.scheduling.Helper.UserActivityLogger;
import bowden.scheduling.main.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The Login class is the controller for the login screen of the application. It handles user authentication
 * and displays appropriate error messages if login fails.
 */
public class Login implements Initializable {
    //private final static ResourceBundle LoginResourceBundle = ResourceBundle.getBundle("login", Locale.getDefault());


    @FXML
    private Label gettimezoneLabel;

    @FXML
    private Label labelTimezone;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField passwordText;

    @FXML
    private Button quitButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField usernameText;

    /**
     * Called when the user clicks the "Login" button. Validates the user's credentials and logs the attempt.
     * If login is successful, loads the main menu screen. Otherwise, displays an error message.
     *
     * @param event the event that triggered this method call
     * @throws SQLException if an error occurs while accessing the database
     * @throws IOException  if an error occurs while loading the main menu screen
     */
    @FXML
    public void login(ActionEvent event) throws SQLException, IOException {
        String username = usernameText.getText();
        String password = passwordText.getText();

        LoginDaoImpl loginDao = new LoginDaoImpl();
        boolean loginSuccessful = LoginDaoImpl.getLogin(username, password);

        UserActivityLogger.logLoginAttempt(username, loginSuccessful); // log login attempt

        if (loginSuccessful) {
            AppointmentsDaoImpl.checkUpcomingAppointments();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/bowden/scheduling/View/mainMenu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();

        } else {
            // Load the appropriate resource bundle for the alerts based on the user's locale
            ResourceBundle alertsBundle;
            if (Locale.getDefault().getLanguage().equals("fr")) {
                alertsBundle = ResourceBundle.getBundle("bowden.scheduling.resources.alerts_fr");
            } else {
                alertsBundle = ResourceBundle.getBundle("bowden.scheduling.resources.alerts_en");
            }

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(alertsBundle.getString("alertTitle"));
            alert.setContentText(alertsBundle.getString("alertMessage"));
            alert.showAndWait();
        }

    }

    /**
     * Handles the quit button action. Displays a confirmation dialog to the user
     * and closes the application if the user confirms the action.
     *
     * @param event The event that triggered the method call.
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
     * Initializes the login screen with the appropriate translated text for the user's locale.
     *
     * @param url            the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get the default resource bundle
        ResourceBundle defaultBundle = ResourceBundle.getBundle("bowden.scheduling.resources.login", Locale.getDefault());

        // Get the user's locale
        Locale userLocale = Locale.getDefault();

        // Load the appropriate resource bundle for the user's locale, using the default bundle as a fallback
        ResourceBundle userBundle;
        if (userLocale.getLanguage().equals("fr")) {
            userBundle = ResourceBundle.getBundle("bowden.scheduling.resources.login", userLocale);
        } else {
            userBundle = ResourceBundle.getBundle("bowden.scheduling.resources.login", userLocale);
        }

        // Set the translated text for each control
        loginLabel.setText(userBundle.getString("loginLabel"));
        passwordLabel.setText(userBundle.getString("passwordLabel"));
        usernameLabel.setText(userBundle.getString("usernameLabel"));
        gettimezoneLabel.setText(userBundle.getString("gettimezoneLabel"));
        loginButton.setText(userBundle.getString("loginButton"));
        quitButton.setText(userBundle.getString("quitButton"));
        ZoneId zoneId = ZoneId.systemDefault();
        String zone = String.valueOf(zoneId);
        labelTimezone.setText(zone);
    }

}
