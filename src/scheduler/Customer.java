package scheduler;

import java.sql.Timestamp;

public class Customer {

    private int customerId;

    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private Timestamp createDate;
    private String createdBy;
    private String lastUpdatedBy;
    private Timestamp lastUpdatedDate;
    private String division;
    private String country;

    /**
     * Customer constructor
     *
     * @param customerId
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param createDate
     * @param createdBy
     * @param lastUpdatedBy
     * @param lastUpdatedDate
     * @param division
     * @param country
     */
    public Customer(
            int customerId, String name, String address, String postalCode, String phone, Timestamp createDate,
            String createdBy, Timestamp lastUpdatedDate, String lastUpdatedBy, String division, String country
    ) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdatedDate = lastUpdatedDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.division = division;
        this.country = country;
    }

    public Customer() {
    }

    public boolean equals(Customer customer) {
        return this.customerId == customer.customerId &&
                this.name.equals(customer.name) &&
                this.address.equals(customer.address) &&
                this.postalCode.equals(customer.postalCode) &&
                this.phone.equals(customer.phone) &&
                this.createDate.equals(customer.createDate) &&
                this.createdBy.equals(customer.createdBy) &&
                this.lastUpdatedDate.equals(customer.lastUpdatedDate) &&
                this.lastUpdatedBy.equals(customer.lastUpdatedBy) &&
                this.division.equals(customer.division) &&
                this.country.equals(customer.country);
    }

    public String toString() {
        return String.format("%s, ID: %d", name, customerId);
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Timestamp getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }
}
