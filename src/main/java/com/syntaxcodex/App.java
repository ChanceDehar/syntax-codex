package com.syntaxcodex;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.syntaxcodex.application.MainApp;

public class App extends Application {

    public void start(Stage stage) {
        Config.initializeFirebase();
        
        stage.initStyle(StageStyle.TRANSPARENT);

        LoginScreen loginScreen = new LoginScreen(stage, () -> showMainApp(stage));
        Scene scene = loginScreen.createScene();
        stage.setScene(scene);
        stage.setTitle("Syntax Codex");
        
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        double centerX = (screenBounds.getWidth() - scene.getWidth()) / 2 + screenBounds.getMinX();
        double centerY = (screenBounds.getHeight() - scene.getHeight()) / 2 + screenBounds.getMinY();
        stage.setX(centerX);
        stage.setY(centerY);
        
        stage.show();
    }

    private void showMainApp(Stage stage) {
        MainApp mainApp = new MainApp(stage);
        Scene scene = mainApp.createScene();
        stage.setScene(scene);
        
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        double centerX = (screenBounds.getWidth() - scene.getWidth()) / 2 + screenBounds.getMinX();
        double centerY = (screenBounds.getHeight() - scene.getHeight()) / 2 + screenBounds.getMinY();
        stage.setX(centerX);
        stage.setY(centerY);
    }

    public static void main(String[] args) {
        launch(args);
    }
}