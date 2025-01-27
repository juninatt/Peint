package se.pbt.peint;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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


    public void undo() {
        if (!undoStack.isEmpty()) {
            BufferedImage lastState = undoStack.pop();
            redoStack.push(SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null));
            gc.drawImage(SwingFXUtils.toFXImage(lastState, null), 0, 0);
        }
    }

    public void redo() {
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

    public void fillCanvas() {
        saveState();
        gc.setFill(drawingColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Fill entire canvas
    }

    public void saveState() {
        // Save the current canvas state to undoStack
        WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, snapshot);
        undoStack.push(SwingFXUtils.fromFXImage(snapshot, null));
        redoStack.clear(); // Clear redoStack for new actions
    }

    public void setDrawingColor(Color color) {
        this.drawingColor = color;
    }
}

