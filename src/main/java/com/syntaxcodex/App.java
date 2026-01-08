package com.syntaxcodex;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public void start(Stage stage) {
        Config.initializeFirebase();

        LoginScreen loginScreen = new LoginScreen(stage, () -> showMainApp(stage));
        stage.setScene(loginScreen.createScene());
        stage.setTitle("Syntax Codex");
        stage.show();
    }

    private void showMainApp(Stage stage) {
        MainApp mainApp = new MainApp(stage);
        stage.setScene(mainApp.createScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}