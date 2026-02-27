package org.example.monikasfrisrsalon2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.monikasfrisrsalon2.a_controller.BookingController;
import org.example.monikasfrisrsalon2.a_controller.LoginController;
import org.example.monikasfrisrsalon2.a_controller.SceneNavigator;
import org.example.monikasfrisrsalon2.b_service.Service;
import org.example.monikasfrisrsalon2.e_repository.*;

import javax.security.auth.login.LoginContext;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        BookingRepo bookingRepo = new BookingRepo();
        CustomerRepo customerRepo = new CustomerRepo();
        HairDresserRepo hairDresserRepo = new HairDresserRepo();
        TreatmentRepo treatmentRepo = new TreatmentRepo();
        OperatorRepo operatorRepo = new OperatorRepo();

        Service service = new Service(
                bookingRepo,
                customerRepo,
                hairDresserRepo,
                treatmentRepo,
                operatorRepo
        );

        Callback<Class<?>, Object> factory = type -> {
            if (type == BookingController.class) return new BookingController(service);
            if (type == LoginController.class) return new LoginController(service);
            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e){
                throw new RuntimeException("Couldnt create controller: " + type.getName(), e);
            }
        };
        SceneNavigator.setControllerFactory(factory);


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Home.fxml"));
        fxmlLoader.setControllerFactory(factory);

        Parent root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.show();
    }
}
