package scheduler;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Customer {

    private int customerId;

    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private String lastUpdatedBy;
    private LocalDateTime lastUpdatedDate;
    private String division;
    private String country;

    /**
     * Customer constructor
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
            int customerId, String name, String address, String postalCode, String phone, LocalDateTime createDate,
            String createdBy, LocalDateTime lastUpdatedDate, String lastUpdatedBy, String division, String country
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

    public String getPhone() { return phone; }

    public void setPhone(String Phone) { this.phone = phone; }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
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

    public LocalDateTime getLastUpdatedDate() { return lastUpdatedDate; }

    public void setLastUpdatedDate(LocalDateTime date) { this.lastUpdatedDate = lastUpdatedDate; }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) { this.division = division; }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }
}
