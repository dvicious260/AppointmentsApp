package bowden.scheduling.Helper;

/**

 CountryStats is a class that represents the statistics of a country, including its name and the number of customers in it.
 */
public class CountryStats {
    /**
     * The name of the country.
     */
    private final String country;
    /**
     * The number of customers in the country.
     */
    private final int customerCount;

    /**
     * Creates a new instance of the CountryStats class with the specified country name and customer count.
     *
     * @param country The name of the country.
     * @param customerCount The number of customers in the country.
     */
    public CountryStats(String country, int customerCount) {
        this.country = country;
        this.customerCount = customerCount;
    }

    /**
     * Gets the name of the country.
     *
     * @return The name of the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the number of customers in the country.
     *
     * @return The number of customers in the country.
     */
    public int getCustomerCount() {
        return customerCount;
    }
}