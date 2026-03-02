package org.example.monikasfrisrsalon2.a_controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.monikasfrisrsalon2.a_controller.SceneNavigator;

import java.io.IOException;

public class TopMenuController {

    @FXML
    private void goHome(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Main.fxml");
    }

    @FXML
    private void goEarnings(ActionEvent event) throws IOException {
        SceneNavigator.switchTo(event, "Home.fxml");
    }
    @FXML
    private void logOut (ActionEvent event) {
        try{
            SceneNavigator.switchTo(event, "/org/example/monikasfrisrsalon2/Login.fxml");} catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
