package org.example.monikasfrisrsalon2.a_controller;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.monikasfrisrsalon2.b_service.Service;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LoginController {
private Service service;
private Timer timer;
private SceneNavigator sceneNavigator;

public LoginController(Service service, Timer timer, SceneNavigator sceneNavigator){
    this.service = service;
    this.timer = timer;
    this.sceneNavigator =sceneNavigator;
}
    @FXML
    private MFXTextField usernameTextfield;
    @FXML
    private MFXPasswordField passwordfield;
    @FXML
    private Label errorLabel;


    @FXML
    protected void onLoginButtonClick(ActionEvent event){
        String username = usernameTextfield.getText();
        String password = passwordfield.getText();
        boolean login_Succes = service.verifyLogin(username, password);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                errorLabel.setText("");
                errorLabel.setOpacity(0);
            }
        };
        if (login_Succes){
            updateHUD();
            try {
                sceneNavigator.switchTo(event, "Home.fxml");}
            catch (IOException e) {
                errorLabel.setOpacity(1);
                errorLabel.setText("Backend problem");
                timer.schedule(task, 50000);
            }
        } else {
            updateHUD();
            errorLabel.setOpacity(1);
            errorLabel.setText("Invalid username or password");
          timer.schedule(task, 50000);
        }
    }



    private void updateHUD() {
        usernameTextfield.clear();
        passwordfield.clear();
    }

}
