package bowden.scheduling.Model;

import java.time.LocalDateTime;

/**
 * Represents an appointment with a customer.
 */
public class Appointments {
    /**
     * The unique ID of the appointment.
     */
    int appointmentID;

    /**
     * The title of the appointment.
     */
    String title;

    /**
     * The description of the appointment.
     */
    String description;

    /**
     * The location of the appointment.
     */
    String location;

    /**
     * The type of the appointment.
     */
    String type;

    /**
     * The ID of the customer associated with the appointment.
     */
    int customerID;

    /**
     * The ID of the user associated with the appointment.
     */
    int userID;

    /**
     * The ID of the contact associated with the appointment.
     */
    int contactID;

    /**
     * The start time of the appointment.
     */
    LocalDateTime start;

    /**
     * The end time of the appointment.
     */
    LocalDateTime end;

    /**
     * Creates a new appointment with the specified properties.
     *
     * @param appointmentID the unique ID of the appointment
     * @param title the title of the appointment
     * @param description the description of the appointment
     * @param location the location of the appointment
     * @param type the type of the appointment
     * @param start the start time of the appointment
     * @param end the end time of the appointment
     * @param customerID the ID of the customer associated with the appointment
     * @param userID the ID of the user associated with the appointment
     * @param contactID the ID of the contact associated with the appointment
     */
    public Appointments(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * Gets the unique ID of the appointment.
     *
     * @return the unique ID of the appointment
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Gets the start time of the appointment.
     *
     * @return the start time of the appointment
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Gets the end time of the appointment.
     *
     * @return the end time of the appointment
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Sets the start time of the appointment.
     *
     * @param start the new start time of the appointment
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Sets the end time of the appointment.
     *
     * @param end the new end time of the appointment
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Sets the unique ID of the appointment.
     *
     * @param appointmentID the new unique ID of the appointment
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
    * Gets the title of the appointment.
    *
    * @return the title of the appointment
    */
    public String getTitle(){
        return title;
    }

    /**
     * Sets the title of this appointment.
     * @param title the title to set for this appointment
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of this appointment.
     * @return the description of this appointment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this appointment.
     * @param description the description to set for this appointment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the location of this appointment.
     * @return the location of this appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of this appointment.
     * @param location the location to set for this appointment
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the type of this appointment.
     * @return the type of this appointment
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of this appointment.
     * @param type the type to set for this appointment
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the ID of the customer associated with this appointment.
     * @return the customer ID for this appointment
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Sets the ID of the customer associated with this appointment.
     * @param customerID the customer ID to set for this appointment
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Gets the ID of the user associated with this appointment.
     * @return the user ID for this appointment
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the ID of the user associated with this appointment.
     * @param userID the user ID to set for this appointment
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Gets the ID of the contact associated with this appointment.
     * @return the contact ID for this appointment
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Sets the ID of the contact associated with this appointment.
     * @param contactID the contact ID to set for this appointment
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}