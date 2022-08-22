package controller;

import DAO.AppointmentDAO;
import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.UserDAO;
import javafx.fxml.Initializable;
import util.AlertUtil;
import util.SceneManagerUtil;
import constants.ScenePathConstants;

public abstract class Controller implements Initializable {
    protected final CountryDAO COUNTRY_DAO = new CountryDAO();
    protected final CustomerDAO CUSTOMER_DAO = new CustomerDAO();
    protected final UserDAO USER_DAO = new UserDAO();
    protected final AppointmentDAO APPOINTMENT_DAO = new AppointmentDAO();
    protected final SceneManagerUtil SCENE_MANAGER = new SceneManagerUtil();
    protected final ScenePathConstants SCENE_PATH_CONSTANTS = new ScenePathConstants();
    protected final AlertUtil ALERTING = new AlertUtil();

    protected String USER;

    public void setUser(String user) {
        USER = user;
    }
}
