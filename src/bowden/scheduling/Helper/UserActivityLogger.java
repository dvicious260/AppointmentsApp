package bowden.scheduling.Helper;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 The UserActivityLogger class logs login attempts by writing the username, timestamp, and login status to a file.
 */
public class UserActivityLogger {

    private static final String FILENAME = "login_activity.txt";

    /**
     * Logs a login attempt to the file.
     * @param username The username used to attempt to login.
     * @param successful Whether the login attempt was successful.
     */
    public static void logLoginAttempt(String username, boolean successful) {
        LocalDateTime timestamp = LocalDateTime.now();
        String status = successful ? "Success" : "Failed";
        String log = String.format("%s - %s: %s\n", timestamp, status, username);

        try {
            FileWriter writer = new FileWriter(FILENAME, true); // append to file
            writer.write(log);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to log file");
            e.printStackTrace();
        }
    }
}