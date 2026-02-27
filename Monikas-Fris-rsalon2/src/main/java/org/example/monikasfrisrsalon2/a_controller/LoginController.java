package org.example.monikasfrisrsalon2.a_controller;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.example.monikasfrisrsalon2.b_service.Service;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LoginController {
    private final Service service;

    public LoginController(Service service){
    this.service = service;
}
    @FXML
    private MFXTextField usernameTextfield;
    @FXML
    private MFXPasswordField passwordfield;
    @FXML
    private Label errorLabel;


    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws Exception {
        boolean Succes = false;
        try {
            Succes = service.verifyLogin(
                    usernameTextfield.getText(),
                    passwordfield.getText()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (Succes) {
            updateHUD();
            SceneNavigator.switchTo(event, "Home.fxml");
        } else {
            updateHUD();
            errorLabel.setOpacity(1);
            errorLabel.setText("Invalid username or password");

            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(e -> {
                errorLabel.setText("");
                errorLabel.setOpacity(0);
            });
            pause.play();
        }
    }


    private void updateHUD() {
        usernameTextfield.clear();
        passwordfield.clear();
    }
}
