package se.pbt.peint;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToolBar;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The main controller for the Peint application.
 *
 * <p>This class handles the application's user interface events, such as saving, loading,
 * and editing the canvas, as well as managing the toolbar and color picker.</p>
 */
public class PeintController {

    @FXML
    private Canvas canvas;

    @FXML
    private BorderPane rootPane;

    @FXML
    private ToolBar toolBar;

    @FXML
    private ColorPicker colorPicker;

    private CanvasManager canvasManager;
    private ToolbarManager toolbarManager;

    /**
     * Initializes the controller and sets up managers for canvas and toolbar interactions.
     */
    public void initialize() {
        canvasManager = new CanvasManager(canvas, rootPane);
        toolbarManager = new ToolbarManager(toolBar, rootPane);

        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction(event -> canvasManager.setDrawingColor(colorPicker.getValue()));
    }

    /**
     * Handles the "Save" action, allowing the user to save the canvas as an image.
     *
     * <p>Displays a file chooser with filters for common formats (PNG, JPEG, BMP),
     * ensuring compatibility and ease of selection for the user.</p>
     */
    @FXML
    private void onSave() {
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
            } catch (IOException e) {
                System.err.println("Failed to save image: " + e.getMessage());
            }
        }
    }

    /**
     * Handles the "Load" action, allowing the user to load an image onto the canvas.
     */
    @FXML
    private void onLoad() {
        // Open FileChooser for image selection
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Read the selected file as an image
                BufferedImage image = ImageIO.read(selectedFile);

                if (image != null) {
                    canvasManager.loadImage(image); // Draw the image on the canvas
                } else {
                    System.err.println("Invalid image file: " + selectedFile.getName());
                }
            } catch (IOException e) {
                System.err.println("Failed to load image: " + e.getMessage());
            }
        }
    }

    /**
     * Undoes the last action on the canvas.
     */
    @FXML
    private void onUndo() {
        canvasManager.undo();
    }

    /**
     * Redoes the last undone action on the canvas.
     */
    @FXML
    private void onRedo() {
        canvasManager.redo();
    }

    /**
     * Activates the bucket fill tool, allowing the user to fill an area of the canvas with a color.
     */
    @FXML
    private void onBucketFill() {
        canvas.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();
            canvasManager.bucketFill(x, y, colorPicker.getValue());
            resetCanvasEvents();
        });
    }

    /**
     * Toggles the toolbar's position between the top and left of the window.
     */
    @FXML
    private void onToggleToolbarPosition() {
        toolbarManager.togglePosition();
    }

    /**
     * Clears the canvas by removing all drawings.
     */
    @FXML
    private void onClearCanvas() {
        canvasManager.clearCanvas();
    }

    /**
     * Resets the canvas to its default state by disabling mouse events.
     */
    private void resetCanvasEvents() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
    }

    /**
     * Extracts the file extension from a given file name.
     */
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0 && dotIndex < fileName.length() - 1) ? fileName.substring(dotIndex + 1).toLowerCase() : null;
    }
}
