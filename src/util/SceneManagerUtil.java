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
