package com.syntaxcodex.application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainApp {

    private Stage stage;

    public MainApp(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene() {
        Button settingsButton = new Button("Settings");
        settingsButton.getStyleClass().add("settings-button");
        settingsButton.setOnAction(e -> showSettings());

        HBox topBar = new HBox();
        topBar.getStyleClass().add("top-bar");
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.getChildren().add(settingsButton);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab searchTab = new Tab("Search");
        SearchView searchView = new SearchView();
        searchTab.setContent(searchView.createView());

        Tab createTab = new Tab("Create");
        CreateView createView = new CreateView();
        createTab.setContent(createView.createView());

        tabPane.getTabs().addAll(searchTab, createTab);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("main-background");
        root.setTop(topBar);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1100, 750);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return scene;
    }

    private void showSettings() {
        SettingsView settingsView = new SettingsView(stage);
        stage.setScene(settingsView.createScene());
    }
}