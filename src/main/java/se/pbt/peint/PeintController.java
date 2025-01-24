package se.pbt.peint;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

public class PeintController {

    @FXML
    private Canvas canvas;

    @FXML
    private BorderPane rootPane;

    @FXML
    private ToolBar toolBar;

    private CanvasManager canvasManager;
    private ToolbarManager toolbarManager;

    public void initialize() {
        canvasManager = new CanvasManager(canvas, rootPane);
        toolbarManager = new ToolbarManager(toolBar, rootPane);
    }

    @FXML
    private void onSave() {
        System.out.println("Save clicked");
    }

    @FXML
    private void onLoad() {
        System.out.println("Load clicked");
    }

    @FXML
    private void onUndo() {
        System.out.println("Undo clicked");
    }

    @FXML
    private void onRedo() {
        System.out.println("Redo clicked");
    }

    @FXML
    private void onFill() {
        System.out.println("Fill clicked");
    }

    @FXML
    private void onToggleToolbarPosition() {
        toolbarManager.togglePosition();
    }

    @FXML
    private void onClearCanvas() {
        canvasManager.clearCanvas();
        System.out.println("Canvas cleared");
    }
}
