package bowden.scheduling.main;

import bowden.scheduling.Helper.JDBC;
import bowden.scheduling.Model.Appointments;
import bowden.scheduling.Model.Customer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/bowden/scheduling/View/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Scheduling App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();
        // Insert a new customer
        //Customer customer = new Customer(4, "John Doe", "123 Main St", "12345", "555-1234", 1, null);
        //boolean insertResult = CustomersDaoImpl.insertCustomer(customer);
        //System.out.println("Insert result: " + insertResult);
        // Update the customer
        //customer.setName("Jane Doe");
        //boolean updateResult = CustomersDaoImpl.updateCustomer(customer);
        //System.out.println("Update result: " + updateResult);

        //Customer retrievedCustomer = CustomersDaoImpl.getCustomer(4);
        //System.out.println("Retrieved customer: " + retrievedCustomer.getName());

        //boolean deleteResult = CustomersDaoImpl.deleteCustomer(4);
        //System.out.println("Delete result: " + deleteResult);

        // Appointment 1
        Appointments appointment1 = new Appointments(3, "Meeting with Bob", "Discuss marketing strategy", "Office", "Meeting", LocalDateTime.of(2023, 4, 20, 10, 30), LocalDateTime.of(2023, 4, 20, 12, 0), 1, 2, 3);
        // Appointment 2
        Appointments appointment2 = new Appointments(4, "Lunch with Alice", "Catch up on latest news", "Restaurant", "Lunch", LocalDateTime.of(2023, 4, 21, 12, 30), LocalDateTime.of(2023, 4, 21, 13, 30), 4, 1, 1);
// Appointment 3
        Appointments appointment3 = new Appointments(5, "Call with Tom", "Discuss project status", "Phone", "Call", LocalDateTime.of(2023, 4, 22, 14, 0), LocalDateTime.of(2023, 4, 22, 15, 0), 6, 2, 2);
        // Insert customer 1
        Customer customer1 = new Customer(4, "John Smith", "123 Main St", "12345", "555-1234", 1, null);


        // Insert customer 2
        Customer customer2 = new Customer(5, "Jane Doe", "456 Elm St", "67890", "555-5678", 2, null);


        // Insert customer 3
        Customer customer3 = new Customer(6, "Bob Johnson", "789 Oak St", "23456", "555-9012", 3, null);

        launch();
        //JDBC.openConnection();
        //JDBC.closeConnection();
    }
}