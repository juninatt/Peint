package se.pbt.peint;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class FloodFillProcessor {
    private final PixelReader pixelReader;
    private final PixelWriter pixelWriter;
    private final int canvasWidth;
    private final int canvasHeight;

    public FloodFillProcessor(PixelReader pixelReader, PixelWriter pixelWriter, int canvasWidth, int canvasHeight) {
        this.pixelReader = pixelReader;
        this.pixelWriter = pixelWriter;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }


    public void fill(double startX, double startY, Color newColor) {
        // Get the target color at the starting point
        Color targetColor = pixelReader.getColor((int) startX, (int) startY);

        if (newColor.equals(targetColor)) {
            return;
        }

        // Create a 2D boolean array to track visited pixels
        boolean[][] visited = new boolean[canvasWidth][canvasHeight];

        // Create a queue to manage the points to be filled
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point((int) startX, (int) startY)); // Add the starting point to the queue

        // Process the queue until it's empty
        while (!queue.isEmpty()) {
            // Remove the next point from the queue
            Point point = queue.poll();
            int x = point.x;
            int y = point.y;

            // Check if the point is out of bounds or already visited
            if (x < 0 || x >= canvasWidth || y < 0 || y >= canvasHeight || visited[x][y]) {
                continue;
            }

            Color currentColor = pixelReader.getColor(x, y);

            if (!currentColor.equals(targetColor)) {
                continue;
            }

            pixelWriter.setColor(x, y, newColor);

            visited[x][y] = true;

            // Add adjacent pixels to the queue for processing
            queue.add(new Point(x + 1, y)); // Right
            queue.add(new Point(x - 1, y)); // Left
            queue.add(new Point(x, y + 1)); // Down
            queue.add(new Point(x, y - 1)); // Up
        }
    }
}
