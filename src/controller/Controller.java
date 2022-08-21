package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.UserDAO;
import javafx.fxml.Initializable;
import util.SceneManager;
import util.ScenePathConstants;

public abstract class Controller implements Initializable {
    protected final CountryDAO COUNTRY_DAO = new CountryDAO();
    protected final CustomerDAO CUSTOMER_DAO = new CustomerDAO();
    protected final UserDAO USER_DAO = new UserDAO();
    protected final SceneManager SCENE_MANAGER = new SceneManager();
    protected final ScenePathConstants SCENE_PATH_CONSTANTS = new ScenePathConstants();

    protected String USER;

    public void setUser(String user) {
        USER = user;
    }
}
