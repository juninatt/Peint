package se.pbt.peint;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

public class CanvasController {

    @FXML
    private Canvas canvas;

    @FXML
    private StackPane rootPane;

    public void initialize() {
        canvas.widthProperty().bind(rootPane.widthProperty());
        canvas.heightProperty().bind(rootPane.heightProperty());

        canvas.setOnMousePressed(event -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.beginPath();
            gc.moveTo(event.getX(), event.getY());
            gc.stroke();
        });

        canvas.setOnMouseDragged(event -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.lineTo(event.getX(), event.getY());
            gc.stroke();
        });
    }
}
