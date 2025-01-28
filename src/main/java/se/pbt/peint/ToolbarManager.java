package se.pbt.peint;

import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

/**
 * Manages the toolbar's behavior and position within the application.
 *
 * <p>This class allows toggling the toolbar's orientation between horizontal
 * (at the top of the window) and vertical (at the left of the window).</p>
 */
public class ToolbarManager {
    private final ToolBar toolBar;
    private final BorderPane rootPane;
    private boolean isToolbarAtTop = true;

    /**
     * Creates a new ToolbarManager to handle the specified toolbar and layout.
     *
     * @param toolBar the toolbar to manage
     * @param rootPane the layout pane containing the toolbar
     */
    public ToolbarManager(ToolBar toolBar, BorderPane rootPane) {
        this.toolBar = toolBar;
        this.rootPane = rootPane;

        toolBar.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
    }

    /**
     * Toggles the toolbar's position between the top and left of the window.
     */
    public void togglePosition() {
        if (isToolbarAtTop) {
            rootPane.setTop(null);
            rootPane.setLeft(toolBar);
            toolBar.setOrientation(javafx.geometry.Orientation.VERTICAL);
        } else {
            rootPane.setLeft(null);
            rootPane.setTop(toolBar);
            toolBar.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        }
        isToolbarAtTop = !isToolbarAtTop;
    }
}
