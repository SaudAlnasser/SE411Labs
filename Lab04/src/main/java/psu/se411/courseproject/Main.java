package psu.se411.courseproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** Entry point for the SE411 course project's JavaFX application. */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label welcome = new Label("Welcome to SE411 Course Project");
        StackPane root = new StackPane(welcome);
        primaryStage.setTitle("SE411 Course Project");
        primaryStage.setScene(new Scene(root, 480, 240));
        primaryStage.show();
    }
}
