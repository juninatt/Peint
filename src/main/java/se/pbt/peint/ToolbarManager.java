package se.pbt.peint;

import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

public class ToolbarManager {
    private final ToolBar toolBar;
    private final BorderPane rootPane;
    private boolean isToolbarAtTop = true;

    public ToolbarManager(ToolBar toolBar, BorderPane rootPane) {
        this.toolBar = toolBar;
        this.rootPane = rootPane;

        toolBar.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
    }

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
