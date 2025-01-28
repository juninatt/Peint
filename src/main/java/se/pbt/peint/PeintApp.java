package se.pbt.peint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main entry point for the Peint application.
 *
 * <p>Peint is a simple, lightweight drawing application inspired by classic Paint tools.
 * It provides basic functionality for creating and editing images, designed to be straightforward
 * and easy to use.</p>
 */
public class PeintApp extends Application {

    /**
     * Starts the application by loading the main interface and displaying the primary stage.
     *
     * @param stage the primary stage for the application
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(PeintApp.class.getResource("peint-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            stage.setTitle("Peint - Your Drawing App");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Launches the application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch();
    }
}
