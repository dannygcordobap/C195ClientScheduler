package scheduler;

public class User {

    private final String username;
    private final int userID;

    /**
     * User object constructor
     *
     * @param username the username of the new user
     * @param userID   the user ID of the new user
     */
    public User(String username, int userID) {
        this.username = username;
        this.userID = userID;
    }

    public String getUsername() {
        return this.username;
    }

    public int getUserID() {
        return this.userID;
    }
}
