package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(loader.load(), 480, 580);
        stage.setTitle("Cinemax — Movie Booking");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void switchScene(String file) throws Exception {
        switchScene(file, 960, 660);
    }

    public static void switchScene(String file, int w, int h) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/" + file));
        Scene scene = new Scene(loader.load(), w, h);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }
}
