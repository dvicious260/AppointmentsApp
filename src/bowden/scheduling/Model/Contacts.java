package bowden.scheduling.Model;

/**
 * A class representing a contact.
 */
public class Contacts {
    int contactID;       // The ID of the contact.
    String contactName;  // The name of the contact.
    String contactEmail; // The email of the contact.

    /**
     * Constructs a new contact object with the specified ID, name, and email.
     *
     * @param contactID The ID of the contact.
     * @param contactName The name of the contact.
     * @param contactEmail The email of the contact.
     */
    public Contacts(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * Returns a string representation of the contact, which is the name of the contact.
     *
     * @return The name of the contact.
     */
    @Override
    public String toString() {
        return contactName;
    }

    /**
     * Returns the ID of the contact.
     *
     * @return The ID of the contact.
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Sets the ID of the contact.
     *
     * @param contactID The ID of the contact.
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * Returns the name of the contact.
     *
     * @return The name of the contact.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the name of the contact.
     *
     * @param contactName The name of the contact.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Returns the email of the contact.
     *
     * @return The email of the contact.
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Sets the email of the contact.
     *
     * @param contactEmail The email of the contact.
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}