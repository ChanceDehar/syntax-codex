package com.syntaxcodex.application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsView {

    private Stage stage;

    public SettingsView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        Label titleLabel = new Label("Settings");
        titleLabel.getStyleClass().add("section-title");

        Label infoLabel = new Label("Syntax Codex v1.0.0");
        infoLabel.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 14px;");

        Label helpLabel = new Label("Help: Store and search your programming cheat sheets");
        helpLabel.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 14px;");

        Button backButton = new Button("Back to Main");
        backButton.setPrefWidth(200);
        backButton.setOnAction(e -> {
            MainApp mainApp = new MainApp(stage);
            stage.setScene(mainApp.createScene());
        });

        VBox layout = new VBox(20);
        layout.getStyleClass().add("content-area");
        layout.setPadding(new Insets(40));
        layout.getChildren().addAll(titleLabel, infoLabel, helpLabel, backButton);

        VBox root = new VBox();
        root.getStyleClass().add("main-background");
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.getChildren().add(layout);

        Scene scene = new Scene(root, 1100, 750);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return scene;
    }
}