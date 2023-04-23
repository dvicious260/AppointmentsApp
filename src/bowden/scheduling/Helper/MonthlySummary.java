package bowden.scheduling.Helper;

/**
 MonthlySummary is a simple Java class representing a summary of monthly appointments for a specific appointment type.
 */
public class MonthlySummary {

    /**
     The month for which the summary is calculated.
     */
    private final String month;
    /**
     The appointment type for which the summary is calculated.
     */
    private final String appointmentType;
    /**
     The total number of appointments for the month and appointment type.
     */
    private final int total;
    /**
     Constructs a new MonthlySummary object with the given month, appointment type, and total.
     @param month the month for which the summary is calculated
     @param appointmentType the appointment type for which the summary is calculated
     @param total the total number of appointments for the month and appointment type
     */
    public MonthlySummary(String month, String appointmentType, int total) {
        this.month = month;
        this.appointmentType = appointmentType;
        this.total = total;
    }
    /**
     Returns the month for which the summary is calculated.
     @return the month
     */
    public String getMonth() {
        return month;
    }
    /**
     Returns the appointment type for which the summary is calculated.
     @return the appointment type
     */
    public String getAppointmentType() {
        return appointmentType;
    }
    /**
     Returns the total number of appointments for the month and appointment type.
     @return the total
     */
    public int getTotal() {
        return total;
    }
}