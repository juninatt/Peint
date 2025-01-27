package se.pbt.peint;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.util.Stack;

public class CanvasManager {
    private final Canvas canvas;
    private final GraphicsContext gc;

    private final Stack<BufferedImage> undoStack = new Stack<>();
    private final Stack<BufferedImage> redoStack = new Stack<>();

    private Color drawingColor;

    public CanvasManager(Canvas canvas, Region parent) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.widthProperty().bind(parent.widthProperty());
        canvas.heightProperty().bind(parent.heightProperty());

        setupDrawing();
    }


    private void setupDrawing() {
        // Configures the canvas to save state and initialize drawing when the mouse is pressed
        canvas.setOnMousePressed(event -> {
            saveState();
            gc.setStroke(drawingColor);
            gc.beginPath();
            gc.moveTo(event.getX(), event.getY());
            gc.stroke();
        });

        // Configures the canvas to draw continuously as the mouse is dragged
        canvas.setOnMouseDragged(event -> {
            gc.setStroke(drawingColor);
            gc.lineTo(event.getX(), event.getY());
            gc.stroke();
        });
    }

    public void undo() {
        // Reverts the canvas to the previous state by popping the last state from the undo stack
        if (!undoStack.isEmpty()) {
            BufferedImage lastState = undoStack.pop();
            redoStack.push(SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null));
            gc.drawImage(SwingFXUtils.toFXImage(lastState, null), 0, 0);
        }
    }

    public void redo() {
        // Restores a previously undone state by popping the next state from the redo stack
        if (!redoStack.isEmpty()) {
            BufferedImage nextState = redoStack.pop();
            undoStack.push(SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null));
            gc.drawImage(SwingFXUtils.toFXImage(nextState, null), 0, 0);
        }
    }

    public void clearCanvas() {
        saveState();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void loadImage(BufferedImage image) {
        saveState();
        gc.drawImage(SwingFXUtils.toFXImage(image, null), 0, 0);
    }

    public void bucketFill(double startX, double startY, Color newColor) {
        // Capture the current state of the canvas as a WritableImage to access pixel data
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage);

        // Initialize tools for reading and writing pixel data from the canvas
        PixelReader pixelReader = writableImage.getPixelReader();
        PixelWriter pixelWriter = gc.getPixelWriter();

        // Delegate the flood fill operation to a dedicated processor
        FloodFillProcessor floodFillProcessor = new FloodFillProcessor(
                pixelReader, pixelWriter, (int) canvas.getWidth(), (int) canvas.getHeight()
        );
        saveState();
        floodFillProcessor.fill(startX, startY, newColor);
    }

    private void saveState() {
        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, snapshot);
        undoStack.push(SwingFXUtils.fromFXImage(snapshot, null));
        redoStack.clear();
    }

    public void setDrawingColor(Color color) {
        this.drawingColor = color;
    }
}
