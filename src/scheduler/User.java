package scheduler;

public class User {

    private String username;
    private String password;
    private int userID;

    /**
     * User object constructor
     * @param username the username of the new user
     * @param password the password of the new user
     * @param userID the user ID of the new user
     */
    public void User(String username, String password, int userID) {
        this.username = username;
        this.password = password;
        this.userID = userID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getUserID() {
        return this.getUserID();
    }
}
