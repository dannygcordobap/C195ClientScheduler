package util;

import controller.AppointmentController;
import controller.Controller;
import controller.CustomerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scheduler.Appointment;
import scheduler.Customer;
import scheduler.User;

import java.io.IOException;

public class SceneManagerUtil {

    /**
     * Changes the scene and passes the active {@link User} to the new view
     *
     * @param event - ActionEvent that caused the scene change
     * @param newSceneRelativePath - {@link constants.ScenePathConstants} constant for the new scene
     * @param user - {@link User} currently logged in
     * @throws IOException - Thrown on read write error
     */
    public void changeScene(ActionEvent event, String newSceneRelativePath, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(newSceneRelativePath));
        Parent parent = loader.load();
        Controller controller = loader.getController();
        controller.setUser(user);
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Changes the scene while passing a {@link Customer} between views
     *
     * @param event - ActionEvent that caused the scene change
     * @param newSceneRelativePath - {@link constants.ScenePathConstants} constant for the new scene
     * @param user - {@link User} currently logged in
     * @param customer - {@link Customer} to pass to new view
     * @throws IOException - Thrown on read write error
     */
    public void changeScene(ActionEvent event,
                            String newSceneRelativePath,
                            User user,
                            Customer customer) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(newSceneRelativePath));
        Parent parent = loader.load();
        CustomerController customerController = loader.getController();
        customerController.setData(customer);
        customerController.setUser(user);
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Changes the scene while passing an {@link Appointment} between views
     *
     * @param event - ActionEvent that caused the scene change
     * @param newSceneRelativePath - {@link constants.ScenePathConstants} constant for the new scene
     * @param user - {@link User} currently logged in
     * @param appointment - {@link Appointment} to pass to new view
     * @throws IOException - Thrown on read write error
     */
    public void changeScene(ActionEvent event,
                            String newSceneRelativePath,
                            User user,
                            Appointment appointment) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(newSceneRelativePath));
        Parent parent = loader.load();
        AppointmentController appointmentController = loader.getController();
        appointmentController.setData(appointment);
        appointmentController.setUser(user);
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
