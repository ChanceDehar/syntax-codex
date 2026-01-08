package com.syntaxcodex.application;

import com.syntaxcodex.LoginScreen;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp {

    private Stage stage;

    public MainApp(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        Button exitButton = new Button("×");
        exitButton.setStyle("-fx-background-color: #5C4033; -fx-text-fill: #D4AF37; -fx-font-size: 18px; " +
                           "-fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8px 12px; " +
                           "-fx-border-radius: 3px; -fx-background-radius: 3px; -fx-min-height: 38px;");
        exitButton.setOnAction(e -> System.exit(0));

        Button logoutButton = new Button("⎋");
        logoutButton.setStyle("-fx-background-color: #5C4033; -fx-text-fill: #D4AF37; -fx-font-size: 16px; " +
                             "-fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8px 12px; " +
                             "-fx-border-radius: 3px; -fx-background-radius: 3px; -fx-min-height: 38px;");
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

        // Tab buttons
        Button searchTabButton = new Button("Search");
        searchTabButton.getStyleClass().addAll("tab-button", "tab-button-selected");
        searchTabButton.setMinHeight(38);
        
        Button createTabButton = new Button("Create");
        createTabButton.getStyleClass().add("tab-button");
        createTabButton.setMinHeight(38);

        HBox tabsGroup = new HBox(15);
        tabsGroup.setAlignment(Pos.CENTER);
        tabsGroup.getChildren().addAll(searchTabButton, createTabButton);

        // Control buttons group
        HBox controlButtonsGroup = new HBox(10);
        controlButtonsGroup.setAlignment(Pos.CENTER);
        controlButtonsGroup.getChildren().addAll(logoutButton, exitButton);

        // Spacer to balance left side so tabs are truly centered
        Region leftSpacer = new Region();
        leftSpacer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        leftSpacer.setMinWidth(0);
        
        // Bind spacer width to match control buttons width
        leftSpacer.prefWidthProperty().bind(controlButtonsGroup.widthProperty());

        // Combined top box with tabs centered and buttons on right
        BorderPane combinedTopBox = new BorderPane();
        combinedTopBox.setPadding(new Insets(10, 15, 10, 15));
        combinedTopBox.setStyle(
            "-fx-background-color: #ac722d;" +
            "-fx-background-radius: 6px;" +
            "-fx-border-color: #8B6F47;" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 6px;"
        );
        combinedTopBox.setLeft(leftSpacer);
        combinedTopBox.setCenter(tabsGroup);
        combinedTopBox.setRight(controlButtonsGroup);

        // Content area for tab views - in a box below buttons
        StackPane contentArea = new StackPane();
        contentArea.setStyle(
            "-fx-background-color: #ac722d;" +
            "-fx-background-radius: 6px;" +
            "-fx-border-color: #8B6F47;" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 6px;"
        );
        contentArea.setPadding(new Insets(20));
        VBox.setVgrow(contentArea, Priority.ALWAYS); // Makes it fill remaining space
        
        SearchView searchView = new SearchView();
        CreateView createView = new CreateView();
        
        contentArea.getChildren().add(searchView.createView());
        
        // Tab switching logic
        searchTabButton.setOnAction(e -> {
            searchTabButton.getStyleClass().add("tab-button-selected");
            createTabButton.getStyleClass().remove("tab-button-selected");
            contentArea.getChildren().clear();
            contentArea.getChildren().add(searchView.createView());
        });
        
        createTabButton.setOnAction(e -> {
            createTabButton.getStyleClass().add("tab-button-selected");
            searchTabButton.getStyleClass().remove("tab-button-selected");
            contentArea.getChildren().clear();
            contentArea.getChildren().add(createView.createView());
        });

        // Main container with gold background
        VBox mainContainer = new VBox(15); // 15px spacing between top box and content
        mainContainer.setPadding(new Insets(15, 20, 15, 20)); // Same padding all around
        mainContainer.setMaxWidth(1020);
        mainContainer.setMaxHeight(700);
        mainContainer.setStyle(
            "-fx-background-color: #c99645;" +
            "-fx-background-radius: 12px;" +
            "-fx-border-color: #8B6F47;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 12px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 20, 0, 0, 8);"
        );
        mainContainer.getChildren().addAll(combinedTopBox, contentArea);

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

        mainContainer.setOnMousePressed(event -> event.consume());
        mainContainer.setOnMouseDragged(event -> event.consume());

        root.getChildren().addAll(backgroundView, mainContainer);
        StackPane.setAlignment(mainContainer, Pos.TOP_CENTER);
        StackPane.setMargin(mainContainer, new Insets(375, 0, 0, 0));

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return scene;
    }
}