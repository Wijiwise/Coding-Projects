package Control;

import javafx.application.Application;
import javafx.stage.Stage;
/**
 * The main entry point for the JavaFX application.
 * This class handles the standard application lifecycle launch and initializes the MVC components.
 * 
 * @author Luis Carlos / Nathan Ng (assuming based on prior files)
 * @version 1.0 (or version of Control package)
 */
public class Main extends Application{
    /**
     * The primary entry point for the JavaFX application thread.
     * 
     * This method is automatically called after {@link #main(String[])} calls
     * {@code launch(args)}. It initializes the {@link MainController} and 
     * starts the game logic.
     *
     *
     * @param stage The primary stage/window for this application.
     */
    @Override
    public void start(Stage stage) {
        MainController controller = new MainController(stage);
        controller.startGame();
    }
    
    /**
     * The standard main method which launches the JavaFX application.
     * 
     * This method calls {@code Application.launch(args)}, which then calls
     * the {@link #start(Stage)} method.
     *
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}