package org.example.monikasfrisrsalon2.a_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.monikasfrisrsalon2.b_service.Service;

public class HelloController {
    @FXML
    private Label welcomeText;
    private Service service;

    public HelloController(Service service) {
        this.service = service;
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
