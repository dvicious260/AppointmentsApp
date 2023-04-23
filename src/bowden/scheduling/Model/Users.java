package bowden.scheduling.Model;

/**
 * Represents a User in the scheduling application.
 */
public class Users {

    /**
     * The ID of this User.
     */
    int userID;

    /**
     * The name of this User.
     */
    String userName;

    /**
     * The password of this User.
     */
    String password;

    /**
     * Creates a new User with the given ID, name, and password.
     *
     * @param userID The ID of the User.
     * @param userName The name of the User.
     * @param password The password of the User.
     */
    public Users(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Returns a string representation of this User, which is its ID.
     *
     * @return The ID of this User.
     */
    @Override
    public String toString() {
        return String.valueOf(userID);
    }

    /**
     * Returns the ID of this User.
     *
     * @return The ID of this User.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the ID of this User.
     *
     * @param userID The new ID for this User.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Returns the name of this User.
     *
     * @return The name of this User.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the name of this User.
     *
     * @param userName The new name for this User.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns the password of this User.
     *
     * @return The password of this User.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of this User.
     *
     * @param password The new password for this User.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
