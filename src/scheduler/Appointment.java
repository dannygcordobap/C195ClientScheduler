package scheduler;

import java.sql.Timestamp;

public class Appointment {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int userId;
    private int customerId;
    private int contactId;

    public Appointment(int appointmentId, String title, String description, String location, String type,
                       Timestamp start, Timestamp end, Timestamp createDate, String createdBy,
                       Timestamp lastUpdate, String lastUpdatedBy, int userId, int customerId, int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.userId = userId;
        this.customerId = customerId;
        this.contactId = contactId;
    }

    public Appointment() {
    }

    public boolean equals(Appointment appointment) {
        return (
                this.appointmentId == appointment.getAppointmentId() &&
                this.title.equals(appointment.getTitle()) &&
                this.description.equals(appointment.getDescription()) &&
                this.location.equals(appointment.getLocation()) &&
                this.type.equals(appointment.getType()) &&
                this.start.equals(appointment.getStart()) &&
                this.end.equals(appointment.getEnd()) &&
                this.createDate.equals(appointment.getCreateDate()) &&
                this.createdBy.equals(appointment.getCreatedBy()) &&
                this.lastUpdate.equals(appointment.getLastUpdate()) &&
                this.lastUpdatedBy.equals(appointment.getLastUpdatedBy()) &&
                this.userId == appointment.getUserId() &&
                this.customerId == appointment.getCustomerId() &&
                this.contactId == appointment.getContactId()
        );
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
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

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
