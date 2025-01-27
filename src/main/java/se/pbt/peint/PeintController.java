package se.pbt.peint;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                BufferedImage image = ImageIO.read(selectedFile);
                canvasManager.loadImage(image);
                System.out.println("Image loaded: " + selectedFile.getName());
            } catch (IOException e) {
                System.err.println("Failed to load image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onUndo() {
        canvasManager.undo();
        System.out.println("Undo performed");
    }

    @FXML
    private void onRedo() {
        canvasManager.redo();
        System.out.println("Redo performed");
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
