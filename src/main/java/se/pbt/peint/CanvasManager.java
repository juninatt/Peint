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

/**
 * Manages the canvas, providing functionality for drawing, undo/redo, and flood fill operations.
 *
 * <p>This class encapsulates all logic related to canvas manipulation and state management.</p>
 */
public class CanvasManager {
    private final Canvas canvas;
    private final GraphicsContext gc;

    private final Stack<BufferedImage> undoStack = new Stack<>();
    private final Stack<BufferedImage> redoStack = new Stack<>();

    private Color drawingColor;

    /**
     * Creates a new CanvasManager to manage the specified canvas and its parent layout.
     *
     * @param canvas the canvas to manage
     * @param parent the parent layout containing the canvas
     */
    public CanvasManager(Canvas canvas, Region parent) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        canvas.widthProperty().bind(parent.widthProperty());
        canvas.heightProperty().bind(parent.heightProperty());

        setupDrawing();
    }

    /**
     * Sets up mouse event handlers for drawing on the canvas.
     *
     * <p>Pressing the mouse starts a new drawing path, and dragging continues the path.</p>
     */
    private void setupDrawing() {
        canvas.setOnMousePressed(event -> {
            saveState();
            gc.setStroke(drawingColor);
            gc.beginPath();
            gc.moveTo(event.getX(), event.getY());
            gc.stroke();
        });

        canvas.setOnMouseDragged(event -> {
            gc.setStroke(drawingColor);
            gc.lineTo(event.getX(), event.getY());
            gc.stroke();
        });
    }


    /**
     * Undoes the last action on the canvas by popping the last state from the undo stack.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            BufferedImage lastState = undoStack.pop();
            redoStack.push(SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null));
            gc.drawImage(SwingFXUtils.toFXImage(lastState, null), 0, 0);
        }
    }

    /**
     * Redoes the last undone action on the canvas by popping the next state from the redo stack.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            BufferedImage nextState = redoStack.pop();
            undoStack.push(SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null));
            gc.drawImage(SwingFXUtils.toFXImage(nextState, null), 0, 0);
        }
    }

    /**
     * Clears all drawings from the canvas.
     */
    public void clearCanvas() {
        saveState();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Loads an image onto the canvas.
     */
    public void loadImage(BufferedImage image) {
        saveState();
        gc.drawImage(SwingFXUtils.toFXImage(image, null), 0, 0);
    }

    /**
     * Fills an area on the canvas with a new color using {@link FloodFillProcessor}.
     *
     * <p>This method captures the current canvas state as an image to access pixel data,
     * initializes tools for reading and writing pixels, and delegates the flood fill
     * operation to a dedicated processor.</p>
     */
    public void bucketFill(double startX, double startY, Color newColor) {
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage);

        PixelReader pixelReader = writableImage.getPixelReader();
        PixelWriter pixelWriter = gc.getPixelWriter();

        FloodFillProcessor floodFillProcessor = new FloodFillProcessor(
                pixelReader, pixelWriter, (int) canvas.getWidth(), (int) canvas.getHeight()
        );
        saveState();
        floodFillProcessor.fill(startX, startY, newColor);
    }

    /**
     * Saves the current state of the canvas for undo functionality.
     *
     * <p>Clears the redo stack to ensure consistency after a new action.</p>
     */
    private void saveState() {
        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, snapshot);
        undoStack.push(SwingFXUtils.fromFXImage(snapshot, null));
        redoStack.clear();
    }

    /**
     * Sets the drawing color for the canvas.
     *
     * @param color the new drawing color
     */
    public void setDrawingColor(Color color) {
        this.drawingColor = color;
    }
}
