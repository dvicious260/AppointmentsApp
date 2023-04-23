package bowden.scheduling.Helper;

import bowden.scheduling.main.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**

 The Methods class provides utility methods for the scheduling application.
 */
public class Methods {
    /**
     * Loads the main menu FXML file and displays it in the primary stage.
     *
     * @param event the ActionEvent triggering the method call
     * @throws IOException if the main menu FXML file cannot be loaded
     */
    public static void home(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/bowden/scheduling/View/mainMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

}
