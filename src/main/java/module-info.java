module com.plano.planocasa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.plano.planocasa to javafx.fxml;
    exports com.plano.planocasa;
}