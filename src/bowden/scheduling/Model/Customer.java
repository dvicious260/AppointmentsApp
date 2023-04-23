package bowden.scheduling.Model;

import bowden.scheduling.DAO.CountriesDaoImpl;
import bowden.scheduling.DAO.DivisionsDaoImpl;
import javafx.collections.ObservableList;

import java.sql.SQLException;

/**
 A class representing a customer.
 */
public class Customer {
    int id;
    String name;
    String address;
    String postalCode;
    String phone;
    int divisionID;
    String divisionName;

    private Countries country;
    FirstLevelDivisions division;

    public Customer() {
        // Blank constructor
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    /**
     * Constructs a Customer object with the given parameters.
     *
     * @param id         the ID of the customer
     * @param name       the name of the customer
     * @param address    the address of the customer
     * @param postalCode the postal code of the customer
     * @param phone      the phone number of the customer
     * @param divisionID the ID of the division associated with the customer
     * @param division   the FirstLevelDivisions object associated with the customer
     */
    public Customer(int id, String name, String address, String postalCode, String phone, int divisionID, FirstLevelDivisions division) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
        //this.divisionName = divisionName;
    }

    /**
     * Returns the country associated with the customer.
     *
     * @return the country associated with the customer
     * @throws SQLException if there is an error accessing the database
     */
    public Countries getCountry() throws SQLException {
        FirstLevelDivisions division = this.getDivision();
        return CountriesDaoImpl.getCountryById(division.getCountryID());
    }

    /**
     * Sets the country associated with the customer.
     *
     * @throws SQLException if there is an error accessing the database
     */
    public void setCountry() throws SQLException {
        if (this.getDivision() != null) {
            int countryId = this.getDivision().getCountryID();
            ObservableList<Countries> countries = CountriesDaoImpl.getAllCountries();
            for (Countries country : countries) {
                if (country.getCountryID() == countryId) {
                    this.country = country;
                    break;
                }
            }
        }
    }

    /**
     * Returns the FirstLevelDivisions object associated with the customer.
     *
     * @return the FirstLevelDivisions object associated with the customer
     * @throws SQLException if there is an error accessing the database
     */
    public FirstLevelDivisions getDivision() throws SQLException {
        return DivisionsDaoImpl.getDivisionById(this.divisionID);
    }

    /**
     * Returns the ID of the customer.
     *
     * @return the ID of the customer
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the customer.
     *
     * @return the name of the customer
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the address of the customer.
     *
     * @return the address of the customer
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the postal code of the customer.
     *
     * @return the postal code of the customer
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Returns the phone number of the customer.
     *
     * @return the phone number of the customer
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the division ID of the customer.
     *
     * @return the division ID of the customer
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Returns the division name of the customer.
     *
     * @return the division name of the customer
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Sets the ID of the customer.
     *
     * @param id the new ID of the customer
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the name of the customer.
     *
     * @param name the new name of the customer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address the new address of the customer
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the postal code of the customer.
     *
     * @param postalCode the new postal code of the customer
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Sets the phone number of the customer.
     *
     * @param phone the new phone number of the customer
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Sets the division ID of the customer.
     *
     * @param divisionID the new division ID of the customer
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * Sets the division name of the customer.
     *
     * @param divisionName the new division name of the customer
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
}