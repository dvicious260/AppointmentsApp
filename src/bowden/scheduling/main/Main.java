package bowden.scheduling.main;

import bowden.scheduling.Helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 The Main class is the entry point of the Scheduling App.
 It launches the application by loading the login screen and opening a new window.
 It also opens a connection to the database using the JDBC utility.
 */
public class Main extends Application {

    /**
     * This method is called when the application is launched.
     * It loads the login screen and creates a new window with the title "Scheduling App".
     * @param stage The primary stage for the application.
     * @throws IOException If the login.fxml file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/bowden/scheduling/View/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Scheduling App");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method opens a connection to the database using the JDBC utility and launches the application.
     * @param args An array of command-line arguments passed to the application.
     * @throws SQLException If there is an error opening the connection to the database.
     */
    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();

        launch();
    }
}