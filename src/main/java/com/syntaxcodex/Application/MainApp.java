package com.syntaxcodex.application;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp {

    private Stage stage;

    public MainApp(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        Label placeholder = new Label("Main App - Coming Soon");

        VBox root = new VBox();
        root.getChildren().add(placeholder);

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return scene;
    }
}