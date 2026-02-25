module org.example.monikasfrisrsalon2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires jdk.jdi;
    requires java.sql;
    requires MaterialFX;
    requires org.kordamp.ikonli.core;
    requires jdk.compiler;

    opens org.example.monikasfrisrsalon2 to javafx.fxml;
    exports org.example.monikasfrisrsalon2;
    exports org.example.monikasfrisrsalon2.a_controller;
    opens org.example.monikasfrisrsalon2.a_controller to javafx.fxml;
}