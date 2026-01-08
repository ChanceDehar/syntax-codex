package com.syntaxcodex.application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp {

    private Stage stage;

    public MainApp(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        Button exitButton = new Button("×");
        exitButton.setStyle("-fx-background-color: #5C4033; -fx-text-fill: #D4AF37; -fx-font-size: 18px; " +
                           "-fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 4px 10px; " +
                           "-fx-border-radius: 3px; -fx-background-radius: 3px;");
        exitButton.setOnAction(e -> System.exit(0));

        Button settingsButton = new Button("⚙");
        settingsButton.getStyleClass().add("settings-button");
        settingsButton.setOnAction(e -> showSettings());

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(10, 15, 0, 0));
        topBar.getChildren().addAll(settingsButton, exitButton);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab searchTab = new Tab("Search");
        SearchView searchView = new SearchView();
        searchTab.setContent(searchView.createView());

        Tab createTab = new Tab("Create");
        CreateView createView = new CreateView();
        createTab.setContent(createView.createView());

        tabPane.getTabs().addAll(searchTab, createTab);

        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(60, 40, 40, 40));
        contentBox.setMaxWidth(900);
        contentBox.setMaxHeight(650);
        contentBox.setStyle("-fx-background-color: #D2B48C; " +
                           "-fx-background-radius: 8px; " +
                           "-fx-border-color: #8B6F47; " +
                           "-fx-border-width: 2px; " +
                           "-fx-border-radius: 8px;");
        contentBox.getChildren().addAll(topBar, tabPane);

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

    private void showSettings() {
        SettingsView settingsView = new SettingsView(stage);
        stage.setScene(settingsView.createScene());
    }
}