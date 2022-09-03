package controller;

import DAO.*;
import javafx.fxml.Initializable;
import scheduler.User;
import util.AlertUtil;
import util.SceneManagerUtil;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public abstract class Controller implements Initializable {
    protected final CountryDAO COUNTRY_DAO = new CountryDAO();
    protected final CustomerDAO CUSTOMER_DAO = new CustomerDAO();
    protected final UserDAO USER_DAO = new UserDAO();
    protected final ContactDAO CONTACT_DAO = new ContactDAO();
    protected final AppointmentDAO APPOINTMENT_DAO = new AppointmentDAO();
    protected final SceneManagerUtil SCENE_MANAGER = new SceneManagerUtil();
    protected final AlertUtil ALERTING = new AlertUtil();

    protected User USER;

    public void setUser(User user) {
        USER = user;
    }

    protected String formatDatetime(Timestamp datetime) {
        String formattedDatetime;
        formattedDatetime = datetime.toLocalDateTime().format(
                DateTimeFormatter.ofPattern("hh:mm a E dd MMM yy")
        );
        return formattedDatetime;
    }

    protected String formatDateTimeTextField(Timestamp datetime) {
        String formattedDatetime;
        formattedDatetime = datetime.toLocalDateTime().format(
                DateTimeFormatter.ofPattern("kk:mm MM/dd/yy")
        );
        return formattedDatetime;
    }
}
