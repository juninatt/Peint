package se.pbt.peint;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ToolBar;
import javafx.scene.image.WritableImage;
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
        // Open a FileChooser to let the user select where to save the file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Canvas As Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("BMP Files", "*.bmp")
        );

        File saveFile = fileChooser.showSaveDialog(null);
        if (saveFile != null) {
            try {
                // Capture the canvas content as an image
                WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, writableImage);

                // Determine the format based on file extension
                String fileExtension = getFileExtension(saveFile.getName());
                if (fileExtension == null) {
                    System.err.println("Unsupported file extension");
                    return;
                }

                // Save the image using ImageIO
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(bufferedImage, fileExtension, saveFile);

                System.out.println("Canvas saved as: " + saveFile.getName());
            } catch (IOException e) {
                System.err.println("Failed to save image: " + e.getMessage());
            }
        }
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


    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0 && dotIndex < fileName.length() - 1) ? fileName.substring(dotIndex + 1).toLowerCase() : null;
    }
}
