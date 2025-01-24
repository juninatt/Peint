module se.pbt.peint {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens se.pbt.peint to javafx.fxml;
    exports se.pbt.peint;
}