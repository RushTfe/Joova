package app;

import controller.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class JoovaApp extends Application {
    private static Stage primaryStage;
    private MainController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        JoovaApp.primaryStage = primaryStage;
        controller = new MainController();

        Scene scene = new Scene(controller.getRoot());

        Image icono = new Image(getClass().getResourceAsStream("/LogoIcono2Joova.png"));
        primaryStage.getIcons().add(icono);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Joova");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
