package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class LoggerUtil {

    /**
     * Verifies existence of the login_activity.txt log file. If the file does not exist
     * it is created and the creation time and date is noted.
     */
    private static void createLogIfDoesNotExist() {
        File logFile = new File("login_activity.txt");
        try {
            if (logFile.createNewFile()) {
                FileWriter writer = new FileWriter("login_activity.txt");
                writer.write(
                        new Timestamp(System.currentTimeMillis()).toString() +
                                ": Initializing new 'login_activity.txt' log file\n");
                writer.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Logs a login attempt in the login_activity.txt document
     *
     * @param username - Username string that was used in the login attempt
     * @param successful - Boolean value indicating whether the attempt was successful
     */
    public static void trackLoginAttempt(String username, boolean successful) {
        createLogIfDoesNotExist();
        try {
            FileWriter writer = new FileWriter("login_activity.txt", true);
            writer.write(String.format(
                    "%s: %s login attempt with username=%s\n",
                    new Timestamp(System.currentTimeMillis()),
                    successful ? "Successful" : "Failed",
                    username
            ));
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}