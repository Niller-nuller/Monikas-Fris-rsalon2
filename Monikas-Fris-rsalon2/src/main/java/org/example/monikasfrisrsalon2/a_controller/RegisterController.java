package org.example.monikasfrisrsalon2.a_controller;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.example.monikasfrisrsalon2.b_service.ServiceLogin;

import java.io.IOException;

public class RegisterController {
    ServiceLogin serviceLogin;
    public RegisterController(ServiceLogin serviceLogin) {
        this.serviceLogin = serviceLogin;
    }
    @FXML
    private MFXTextField usernameTextfield;
    @FXML
    private MFXPasswordField passwordfield;
    @FXML
    private Label errorLabel;

    @FXML
    public void onSignUpButton(javafx.event.ActionEvent event) {
        try{
        serviceLogin.createOperator(serviceLogin.createOperator(usernameTextfield.getText(), passwordfield.getText()));
            try {
                SceneNavigator.switchTo(event, "/org/example/monikasfrisrsalon2/login.fxml");
            } catch (IOException e) {
                errorLabel.setText("IO problem");
                errorLabel.setOpacity(1);
                updateHUD(event);
            }} catch(Exception e){
            errorLabel.setText("DB problem");
            errorLabel.setOpacity(1);
            updateHUD(event);
        }

    }
    private void updateHUD(ActionEvent event) {
        usernameTextfield.clear();
        passwordfield.clear();
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            errorLabel.setText("");
            errorLabel.setOpacity(0);
        });
        pause.play();
    }
}
