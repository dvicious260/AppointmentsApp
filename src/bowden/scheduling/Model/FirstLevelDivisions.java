package bowden.scheduling.Model;

import bowden.scheduling.DAO.CountriesDaoImpl;

import java.sql.SQLException;

/**
 * A class representing a first-level division. A first-level division is a geographic area containing smaller
 * subdivisions, such as states or provinces, that are used for organization and reporting purposes. This class
 * contains information about the division's ID, name, and associated country.
 */
public class FirstLevelDivisions {

    /**
     * The ID of the first-level division.
     */
    private int divisionID;

    /**
     * The name of the first-level division.
     */
    private String divisionName;

    /**
     * The ID of the country associated with the first-level division.
     */
    private int countryID;

    /**
     * Constructs a new first-level division with the specified ID, name, and associated country.
     *
     * @param divisionID  the ID of the first-level division
     * @param divisionName  the name of the first-level division
     * @param countryID  the ID of the country associated with the first-level division
     */
    public FirstLevelDivisions(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    /**
     * Returns the ID of the first-level division.
     *
     * @return the ID of the first-level division
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Sets the ID of the first-level division.
     *
     * @param divisionID  the ID of the first-level division to set
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * Returns the name of the first-level division.
     *
     * @return the name of the first-level division
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Sets the name of the first-level division.
     *
     * @param divisionName  the name of the first-level division to set
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * Gets the ID of the country to which this division belongs.
     * @return the ID of the country
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Sets the ID of the country to which this division belongs.
     * @param countryID the ID of the country
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * Returns a string representation of this division.
     * @return the division name
     */
    @Override
    public String toString() {
        return divisionName;
    }

    /**
     * Retrieves the country to which this division belongs.
     * @return the country object
     * @throws SQLException if an error occurs while retrieving the country from the database
     */
    public Countries getCountry() throws SQLException {
        return CountriesDaoImpl.getCountryById(this.getCountryID());
    }
}