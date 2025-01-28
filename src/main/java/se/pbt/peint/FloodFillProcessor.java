package se.pbt.peint;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Performs flood fill operations on a canvas.
 *
 * <p>This class processes pixel data from a canvas using a flood fill
 * algorithm to replace a contiguous area of color with a new color.</p>
 */
public class FloodFillProcessor {
    private final PixelReader pixelReader;
    private final PixelWriter pixelWriter;
    private final int canvasWidth;
    private final int canvasHeight;

    /**
     * Initializes the processor with canvas dimensions and pixel data handlers.
     */
    public FloodFillProcessor(PixelReader pixelReader, PixelWriter pixelWriter, int canvasWidth, int canvasHeight) {
        this.pixelReader = pixelReader;
        this.pixelWriter = pixelWriter;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    /**
     * Fills a contiguous area starting at the given coordinates with a new color.
     *
     * <p>The algorithm replaces all connected pixels of the same initial color
     * with the specified new color.</p>
     */
    public void fill(double startX, double startY, Color newColor) {
        Color targetColor = pixelReader.getColor((int) startX, (int) startY);

        if (newColor.equals(targetColor)) {
            return;
        }

        boolean[][] visited = new boolean[canvasWidth][canvasHeight];
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point((int) startX, (int) startY));

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            int x = point.x;
            int y = point.y;

            if (shouldSkipPixel(x, y, visited, targetColor)) {
                continue;
            }

            fillPixel(x, y, newColor, visited);
            enqueueNeighbors(queue, x, y);
        }
    }

    /**
     * Checks if a pixel should be skipped during processing.
     */
    private boolean shouldSkipPixel(int x, int y, boolean[][] visited, Color targetColor) {
        if (x < 0 || x >= canvasWidth || y < 0 || y >= canvasHeight || visited[x][y]) {
            return true;
        }
        Color currentColor = pixelReader.getColor(x, y);
        return !currentColor.equals(targetColor);
    }

    /**
     * Fills a single pixel with the new color and marks it as visited.
     */
    private void fillPixel(int x, int y, Color newColor, boolean[][] visited) {
        pixelWriter.setColor(x, y, newColor);
        visited[x][y] = true;
    }

    /**
     * Adds the neighboring pixels (up, down, left, right) to the processing queue.
     */
    private void enqueueNeighbors(Queue<Point> queue, int x, int y) {
        queue.add(new Point(x + 1, y)); // Right
        queue.add(new Point(x - 1, y)); // Left
        queue.add(new Point(x, y + 1)); // Down
        queue.add(new Point(x, y - 1)); // Up
    }
}
