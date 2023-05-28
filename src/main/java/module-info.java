module com.example.endofthetrack_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires junit;

    opens com.example.endofthetrack_project to javafx.fxml;
    exports com.example.endofthetrack_project;
    exports com.example.endofthetrack_project.Controller;
    opens com.example.endofthetrack_project.Controller to javafx.fxml;
    exports com.example.endofthetrack_project.View;
    opens com.example.endofthetrack_project.View to javafx.fxml;
    exports com.example.endofthetrack_project.Tests;
    opens com.example.endofthetrack_project.Tests to javafx.fxml;
}