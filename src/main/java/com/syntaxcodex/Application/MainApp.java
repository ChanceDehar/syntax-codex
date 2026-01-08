package com.syntaxcodex.application;

import com.syntaxcodex.LoginScreen;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        Button logoutButton = new Button("⎋");
        logoutButton.setStyle("-fx-background-color: #5C4033; -fx-text-fill: #D4AF37; -fx-font-size: 16px; " +
                             "-fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 6px 12px; " +
                             "-fx-border-radius: 3px; -fx-background-radius: 3px;");
        logoutButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen(stage, () -> {
                javafx.scene.Scene mainScene = createScene();
                stage.setScene(mainScene);
                
                javafx.application.Platform.runLater(() -> {
                    javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                    double centerX = (screenBounds.getWidth() - mainScene.getWidth()) / 2 + screenBounds.getMinX();
                    double centerY = (screenBounds.getHeight() - mainScene.getHeight()) / 2 + screenBounds.getMinY();
                    stage.setX(centerX);
                    stage.setY(centerY);
                });
            });
            javafx.scene.Scene scene = loginScreen.createScene();
            stage.setScene(scene);
            
            javafx.application.Platform.runLater(() -> {
                javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
                double centerX = (screenBounds.getWidth() - scene.getWidth()) / 2 + screenBounds.getMinX();
                double centerY = (screenBounds.getHeight() - scene.getHeight()) / 2 + screenBounds.getMinY();
                stage.setX(centerX);
                stage.setY(centerY);
            });
        });

        Button settingsButton = new Button("⚙");
        settingsButton.getStyleClass().add("settings-button");
        settingsButton.setOnAction(e -> showSettings());

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(10, 15, 0, 0));
        topBar.getChildren().addAll(logoutButton, settingsButton, exitButton);

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
        contentBox.setPadding(new Insets(30, 40, 30, 40));
        contentBox.setMaxWidth(900);
        contentBox.setMaxHeight(550);
        contentBox.setStyle("-fx-background-color: transparent;");
        contentBox.getChildren().addAll(topBar, tabPane);

        Image backgroundImage = new Image(getClass().getResourceAsStream("/MainBackground.png"));
        
        double sceneWidth = backgroundImage.getWidth();
        double sceneHeight = backgroundImage.getHeight();

        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(sceneWidth);
        backgroundView.setFitHeight(sceneHeight);
        backgroundView.setPreserveRatio(false);

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

        root.getChildren().addAll(backgroundView, contentBox);
        StackPane.setAlignment(contentBox, Pos.TOP_CENTER);
        StackPane.setMargin(contentBox, new Insets(340, 0, 0, 0));

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