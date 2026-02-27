package org.example.monikasfrisrsalon2.a_controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public final class SceneNavigator {

    private static Callback<Class<?>, Object> controllerFactory;

    private SceneNavigator() {
    }

    public static void setControllerFactory(Callback<Class<?>, Object> factory) {
        controllerFactory = factory;
    }

    public static void switchTo(ActionEvent event, String fxmlName) throws IOException {
        FXMLLoader loader = createLoader(fxmlName);
        Parent root = loader.load();
        switchStageScene(event, root);
    }

    public static <T> void switchTo(
            ActionEvent event,
            String fxmlName,
            Consumer<T> controllerInitializer) throws IOException {

        FXMLLoader loader = createLoader(fxmlName);
        Parent root = loader.load();
        T controller = loader.getController();
        controllerInitializer.accept(controller);
        switchStageScene(event, root);
    }

    private static FXMLLoader createLoader(String fxmlName) {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(SceneNavigator.class.getResource(fxmlName))
        );
        if (controllerFactory != null) {
            loader.setControllerFactory(controllerFactory);
        }
        return loader;
    }

    private static void switchStageScene(ActionEvent event, Parent root) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Platform.runLater(() -> {
            stage.toFront();
            stage.requestFocus();
        });
    }
}
