package org.example.monikasfrisrsalon2.a_controller;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.example.monikasfrisrsalon2.b_service.ServiceLogin;
import org.example.monikasfrisrsalon2.c_model.Operator;


import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;

import static javafx.scene.input.KeyCode.ESCAPE;


public class LoginController {
    private final ServiceLogin serviceLogin;

    public LoginController(ServiceLogin serviceLogin) {
        this.serviceLogin = serviceLogin;
    }

    @FXML
    private MFXTextField usernameTextfield;
    @FXML
    private MFXPasswordField passwordfield;
    @FXML
    private Label errorLabel;

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        try {
            boolean succes = serviceLogin.login(serviceLogin.createOperator(
                    usernameTextfield.getText().trim(),
                    passwordfield.getText()));
            if (succes) {
                SceneNavigator.switchTo(event, "/org/example/monikasfrisrsalon2/Home.fxml");
                updateHUD(event);
            } else {
                errorLabel.setText("Invalid username or password");
                errorLabel.setOpacity(1);
                updateHUD(event);
            }
        } catch (SQLException e) {
            errorLabel.setText("Database error");
            errorLabel.setOpacity(1);
            updateHUD(event);
        } catch (IOException e) {
            errorLabel.setText("IO error");
            errorLabel.setOpacity(1);
            updateHUD(event);
        }
    }
    @FXML
    private void handleRegistry() {

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
