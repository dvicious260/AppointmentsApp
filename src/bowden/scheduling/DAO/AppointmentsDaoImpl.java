package bowden.scheduling.DAO;

import bowden.scheduling.Helper.DateTime;
import bowden.scheduling.Helper.JDBC;
import bowden.scheduling.Helper.MonthlySummary;
import bowden.scheduling.Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.time.*;
import java.util.*;

public class AppointmentsDaoImpl {
    public static ObservableList<Appointments> getAllAppointments(String filter) {
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        String sqlGetAppointments = "SELECT * FROM appointments";
        if (!filter.isEmpty()) {
            sqlGetAppointments += " WHERE " + filter;
        }
        try {
            PreparedStatement ps = JDBC.openConnection().prepareStatement(sqlGetAppointments);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                Timestamp startTimestamp = rs.getTimestamp("Start", cal);
                Timestamp endTimestamp = rs.getTimestamp("End", cal);
                LocalDateTime start = DateTime.convertFromUTCtoLocal(startTimestamp);
                LocalDateTime end = DateTime.convertFromUTCtoLocal(endTimestamp);
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
                //System.out.println(appointment.getStart());
                allAppointments.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allAppointments;
    }

    public static boolean insertAppointment(Appointments appointment) throws SQLException {
        Connection conn = JDBC.openConnection();
        if (appointment.getEnd().isBefore(appointment.getStart())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The end date or time cannot be before the start date or time.", ButtonType.OK);
            alert.setTitle("Date and Time error");
            alert.showAndWait();
            return false;
        }

        // check if the appointment overlaps with any existing appointments for the same customer
        String checkOverlapSql = "SELECT * FROM appointments WHERE Customer_ID = ? AND (? BETWEEN Start AND End OR ? BETWEEN Start AND End)";
        PreparedStatement checkOverlapPs = conn.prepareStatement(checkOverlapSql);
        checkOverlapPs.setInt(1, appointment.getCustomerID());
        checkOverlapPs.setString(2, DateTime.convertLocalToUTC(appointment.getStart(), ZoneId.systemDefault()).toString());
        checkOverlapPs.setString(3, DateTime.convertLocalToUTC(appointment.getEnd(), ZoneId.systemDefault()).toString());
        ResultSet overlapRs = checkOverlapPs.executeQuery();
        if (overlapRs.next()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The appointment overlaps with an existing appointment for the same customer.", ButtonType.OK);
            alert.setTitle("Existing appointment error");
            alert.showAndWait();

            return false;
        }
        // check if the appointment spans multiple days
        if (!appointment.getStart().toLocalDate().equals(appointment.getEnd().toLocalDate())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment spans multiple days");
            alert.setHeaderText("Can't Schedule Appointment");
            alert.setContentText("The appointment cannot span multiple days.");
            alert.showAndWait();
            return false;
        }

        // get the business hours in EST
        ObservableList<LocalTime> businessHours = DateTime.getBusinessHoursInTimeZone(ZoneId.of("America/New_York"));
        LocalTime startBusinessHoursEST = businessHours.get(0);
        LocalTime endBusinessHoursEST = businessHours.get(businessHours.size() - 1);

        // convert the appointment start and end times to EST
        ZonedDateTime startAppointmentEST = appointment.getStart().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime endAppointmentEST = appointment.getEnd().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("America/New_York"));

        // check if the appointment start and end times fall within the business hours in EST
        if (startAppointmentEST.toLocalTime().isBefore(startBusinessHoursEST) || endAppointmentEST.toLocalTime().isAfter(endBusinessHoursEST)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Outside of business hours");
            alert.setHeaderText("Can't Schedule Appointment");
            alert.setContentText("The time you entered is outside of business hours.\nThe business hours are 8AM-10PM EST");
            alert.showAndWait();

            return false;
        }

        // convert the appointment start and end times back to local time
        ZonedDateTime startAppointmentLocal = startAppointmentEST.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime endAppointmentLocal = endAppointmentEST.withZoneSameInstant(ZoneId.systemDefault());

        // if there are no overlaps, insert the new appointment
        String insertSql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?, NOW(), USER(), NOW(), USER(), ?,?,?)";
        PreparedStatement insertPs = conn.prepareStatement(insertSql);
        insertPs.setInt(1, appointment.getAppointmentID());
        insertPs.setString(2, appointment.getTitle());
        insertPs.setString(3, appointment.getDescription());
        insertPs.setString(4, appointment.getLocation());
        insertPs.setString(5, appointment.getType());
        insertPs.setObject(6, startAppointmentLocal);
        insertPs.setObject(7, endAppointmentLocal);
        insertPs.setInt(8, appointment.getCustomerID());
        insertPs.setInt(8, appointment.getCustomerID());
        insertPs.setInt(9, appointment.getUserID());
        insertPs.setInt(10, appointment.getContactID());

        int rowsInserted = insertPs.executeUpdate();

        System.out.println("Appointment added");
        return rowsInserted > 0; // return true if the insert succeeded
    }


    public static boolean updateAppointment(Appointments appointment) throws SQLException {
        Connection conn = JDBC.openConnection();

        if (appointment.getEnd().isBefore(appointment.getStart())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The end date or time cannot be before the start date or time.", ButtonType.OK);
            alert.setTitle("Date and Time error");
            alert.showAndWait();
            return false;
        }

        // check if the appointment overlaps with any existing appointments for the same customer
        String checkOverlapSql = "SELECT * FROM appointments WHERE Customer_ID = ? AND Appointment_ID <> ? AND (? BETWEEN Start AND End OR ? BETWEEN Start AND End)\n";
        PreparedStatement checkOverlapPs = conn.prepareStatement(checkOverlapSql);
        checkOverlapPs.setInt(1, appointment.getCustomerID());
        checkOverlapPs.setInt(2, appointment.getAppointmentID());
        checkOverlapPs.setTimestamp(3, Timestamp.valueOf(appointment.getStart()));
        checkOverlapPs.setTimestamp(4, Timestamp.valueOf(appointment.getEnd()));
        ResultSet overlapRs = checkOverlapPs.executeQuery();
        if (overlapRs.next()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The appointment overlaps with an existing appointment for the same customer.", ButtonType.OK);
            alert.setTitle("Existing appointment error");
            alert.showAndWait();

            return false;
        }

        // convert the appointment start and end times to EST
        ZonedDateTime startAppointmentEST = appointment.getStart().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime endAppointmentEST = appointment.getEnd().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("America/New_York"));

// get the business hours in EST
        ObservableList<LocalTime> businessHours = DateTime.getBusinessHoursInTimeZone(ZoneId.of("America/New_York"));
        LocalTime startBusinessHours = businessHours.get(0);
        LocalTime endBusinessHours = businessHours.get(businessHours.size() - 1);

// check if the appointment start and end times fall within the business hours in EST
        if (startAppointmentEST.toLocalTime().isBefore(startBusinessHours) || endAppointmentEST.toLocalTime().isAfter(endBusinessHours)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Outside of business hours");
            alert.setHeaderText("Can't Schedule Appointment");
            alert.setContentText("The time you entered is outside of business hours.\nThe business hours are 8AM-10PM EST");
            alert.showAndWait();
            return false;
        }

// convert the appointment start and end times back to the user's local time zone
        ZonedDateTime startAppointmentLocal = startAppointmentEST.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime endAppointmentLocal = endAppointmentEST.withZoneSameInstant(ZoneId.systemDefault());

        // check if the appointment start and end times are on the same day
        if (!startAppointmentLocal.toLocalDate().equals(endAppointmentLocal.toLocalDate())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment spans multiple days");
            alert.setHeaderText("Can't Schedule Appointment");
            alert.setContentText("The appointment cannot span multiple days.");
            alert.showAndWait();
            return false;
        }

        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = NOW(), Last_Updated_By = USER(), Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, Timestamp.valueOf(startAppointmentLocal.toLocalDateTime()));
        ps.setTimestamp(6, Timestamp.valueOf(endAppointmentLocal.toLocalDateTime()));
        ps.setInt(7, appointment.getCustomerID());
        ps.setInt(8, appointment.getUserID());
        ps.setInt(9, appointment.getContactID());
        ps.setInt(10, appointment.getAppointmentID());

        int rowsUpdated = ps.executeUpdate();

        return rowsUpdated > 0;
    }


    public static Appointments getAppointment(int appointmentID) throws SQLException {
        Connection conn = JDBC.openConnection();
        String query = "SELECT * FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, appointmentID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            return new Appointments(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
        }
        return null;
    }

    public static boolean deleteAppointment(int appointmentID) throws SQLException {
        Appointments appointmentToDelete = getAppointment(appointmentID);
        String appointmentIDString = String.valueOf(appointmentID);
        String appointmentType = appointmentToDelete.getType();
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        int rowsDeleted = ps.executeUpdate();

        Alert deletedAlert = new Alert(Alert.AlertType.INFORMATION);
        deletedAlert.setTitle("Appointment Canceled");
        deletedAlert.setHeaderText("Appointment canceled successfully");
        deletedAlert.setContentText(appointmentType + " appointment with ID " + appointmentIDString + " has been canceled");
        deletedAlert.showAndWait();

        return rowsDeleted > 0;

    }

    public static int getMaxAppointmentID() throws SQLException {
        int maxId = 0;
        try (Connection connection = JDBC.openConnection();
             Statement statement = connection.createStatement()) {
            String query = "SELECT MAX(Appointment_ID) AS Max_ID FROM appointments";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                maxId = resultSet.getInt("Max_ID");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            throw ex;
        }
        return maxId;
    }

    public static void checkUpcomingAppointments() {
        ObservableList<Appointments> appointments = getAllAppointments("");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fifteenMinutesFromNow = now.plusMinutes(15);
        boolean hasUpcomingAppointments = false;

        for (Appointments appointment : appointments) {
            if (appointment.getStart().isBefore(fifteenMinutesFromNow) && appointment.getStart().isAfter(now)) {
                hasUpcomingAppointments = true;
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upcoming appointment");
                alert.setHeaderText("You have an appointment coming up!");
                alert.setContentText("You have an appointment with id " + appointment.getAppointmentID() + " titled " + appointment.getTitle() + " in less than 15 minutes at " + appointment.getStart().toLocalTime() + " on " + appointment.getStart().toLocalDate() + ".");
                Optional<ButtonType> result = alert.showAndWait();
                break; // exit loop after first alert
            }
        }

        if (!hasUpcomingAppointments) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No upcoming appointments");
            alert.setHeaderText("No upcoming appointments");
            alert.setContentText("You have no appointments within the next 15 minutes");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    public List<MonthlySummary> getMonthlySummary() throws SQLException {
        List<MonthlySummary> monthlySummaries = new ArrayList<>();
        Connection conn = JDBC.openConnection();
        String sql = "SELECT MONTH(start) as month, type, COUNT(*) as total FROM appointments GROUP BY MONTH(start), type";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int monthInt = rs.getInt("month");
                String month = Month.of(monthInt).toString();
                String type = rs.getString("type");
                int total = rs.getInt("total");
                monthlySummaries.add(new MonthlySummary(month, type, total));
            }
        }
        return monthlySummaries;
    }

    public static ObservableList<Appointments> getAppointmentsByCustomer(int contactID) {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
        try {
            PreparedStatement ps = JDBC.openConnection().prepareStatement(sql);
            ps.setInt(1, contactID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                Timestamp startTimestamp = rs.getTimestamp("Start", cal);
                Timestamp endTimestamp = rs.getTimestamp("End", cal);
                LocalDateTime start = DateTime.convertFromUTCtoLocal(startTimestamp);
                LocalDateTime end = DateTime.convertFromUTCtoLocal(endTimestamp);
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contact = rs.getInt("Contact_ID");
                Appointments appointment = new Appointments(appointmentID, title, description, location, type, start, end, customerID, userID, contact);
                appointments.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointments;
    }


}
