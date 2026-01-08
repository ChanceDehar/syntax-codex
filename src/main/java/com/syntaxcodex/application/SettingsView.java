package com.syntaxcodex.application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SettingsView {

    private Stage stage;

    public SettingsView(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        Button exitButton = new Button("×");
        exitButton.setStyle("-fx-background-color: #5C4033; -fx-text-fill: #D4AF37; -fx-font-size: 18px; " +
                           "-fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 4px 10px; " +
                           "-fx-border-radius: 3px; -fx-background-radius: 3px;");
        exitButton.setOnAction(e -> System.exit(0));

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(10, 15, 0, 0));
        topBar.getChildren().add(exitButton);

        Label titleLabel = new Label("Settings");
        titleLabel.getStyleClass().add("section-title");

        Label infoLabel = new Label("Syntax Codex v1.0.0");
        infoLabel.setStyle("-fx-text-fill: #6D4C41; -fx-font-size: 13px;");

        Label helpLabel = new Label("Help: Store and search your programming cheat sheets");
        helpLabel.setStyle("-fx-text-fill: #6D4C41; -fx-font-size: 13px;");

        Button backButton = new Button("Back to Main");
        backButton.setPrefWidth(200);
        backButton.setOnAction(e -> {
            MainApp mainApp = new MainApp(stage);
            stage.setScene(mainApp.createScene());
        });

        VBox contentLayout = new VBox(20);
        contentLayout.setAlignment(Pos.CENTER);
        contentLayout.getChildren().addAll(titleLabel, infoLabel, helpLabel, backButton);

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(60, 80, 80, 80));
        contentBox.setMaxWidth(700);
        contentBox.setMaxHeight(500);
        contentBox.setStyle("-fx-background-color: #D2B48C; " +
                           "-fx-background-radius: 8px; " +
                           "-fx-border-color: #8B6F47; " +
                           "-fx-border-width: 2px; " +
                           "-fx-border-radius: 8px;");
        contentBox.getChildren().addAll(topBar, contentLayout);

        Image backgroundImage = new Image(getClass().getResourceAsStream("/MainBackground.png"));
        
        double sceneWidth = backgroundImage.getWidth();
        double sceneHeight = backgroundImage.getHeight();
        
        BackgroundImage bg = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(sceneWidth, sceneHeight, false, false, false, false)
        );

        StackPane root = new StackPane();
        root.setPrefSize(sceneWidth, sceneHeight);
        root.setMinSize(sceneWidth, sceneHeight);
        root.setMaxSize(sceneWidth, sceneHeight);
        
        final double[] xOffset = {0};
        final double[] yOffset = {0};

        root.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset[0]);
            stage.setY(event.getScreenY() - yOffset[0]);
        });

        contentBox.setOnMousePressed(event -> event.consume());
        contentBox.setOnMouseDragged(event -> event.consume());

        root.setBackground(new Background(bg));
        root.getChildren().add(contentBox);
        StackPane.setAlignment(contentBox, Pos.CENTER);

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return scene;
    }
}