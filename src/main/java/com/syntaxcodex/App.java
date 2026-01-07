package com.syntaxcodex;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    public void start(Stage stage) {
        Label label = new Label("SyntaxCodex coming soon");
        
        StackPane layout = new StackPane();
        layout.getChildren().add(label);

        Scene scene = new Scene(layout, 800, 600);

        stage.setTitle("Syntax Codex");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}