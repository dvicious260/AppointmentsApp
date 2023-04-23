package bowden.scheduling.Helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.time.*;

/**
 A helper class for various date and time operations.
 */
public class DateTime {

    /**
     * Converts the given LocalDateTime from the local time zone to UTC.
     *
     * @param localDateTime the local date and time to convert
     * @param localZoneId the time zone of the local date and time
     * @return the corresponding date and time in UTC
     */
    public static LocalDateTime convertLocalToUTC(LocalDateTime localDateTime, ZoneId localZoneId) {
        // Convert the input LocalDateTime to an Instant in the local time zone
        Instant localInstant = localDateTime.atZone(localZoneId).toInstant();

        // Convert the local Instant to an Instant in UTC
        Instant utcInstant = localInstant.atZone(ZoneId.of("UTC")).toInstant();

        // Convert the UTC Instant to a LocalDateTime in the system default time zone
        return LocalDateTime.ofInstant(utcInstant, ZoneId.systemDefault());
    }

    /**
     * Converts the given timestamp from UTC to the local time zone.
     *
     * @param timestamp the timestamp in UTC
     * @return the corresponding date and time in the local time zone
     */
    public static LocalDateTime convertFromUTCtoLocal(Timestamp timestamp) {
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZoneId localZoneId = ZoneId.systemDefault();

        Instant instant = timestamp.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, utcZoneId);
        ZonedDateTime zonedDateTime = localDateTime.atZone(utcZoneId);
        ZonedDateTime convertedDateTime = zonedDateTime.withZoneSameInstant(localZoneId);

        return convertedDateTime.toLocalDateTime();
    }


    /**
     * Returns a list of local times representing the business hours of a day in the default time zone.
     *
     * @return an observable list of local times representing the business hours
     */
    public static ObservableList<LocalTime> getBusinessHours() {
        ObservableList<LocalTime> businessHours = FXCollections.observableArrayList();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);
        ZoneId EasternST = ZoneId.of("America/New_York");
        LocalDate today = LocalDate.now();
        while (start.isBefore(end)) {
            LocalDateTime local = LocalDateTime.of(today, start);
            ZonedDateTime zone = ZonedDateTime.of(local, EasternST);
            businessHours.add(zone.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
            start = start.plusMinutes(15);
        }
        return businessHours;
    }

    /**
     * Returns a list of local times representing the business hours of a day in the given time zone.
     *
     * @param timeZone the time zone for which to calculate the business hours
     * @return an observable list of local times representing the business hours
     */
    public static ObservableList<LocalTime> getBusinessHoursInTimeZone(ZoneId timeZone) {
        ObservableList<LocalTime> businessHours = FXCollections.observableArrayList();
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);
        LocalDate today = LocalDate.now(ZoneId.of("America/New_York"));
        while (start.isBefore(end)) {
            LocalDateTime local = LocalDateTime.of(today, start);
            ZonedDateTime zone = ZonedDateTime.of(local, timeZone);
            businessHours.add(zone.withZoneSameInstant(ZoneId.of("America/New_York")).toLocalTime());
            start = start.plusMinutes(15);
        }
        return businessHours;
    }

}