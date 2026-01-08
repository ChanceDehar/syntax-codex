package com.syntaxcodex.application;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CreateView {

    public VBox createView() {
        Label titleLabel = new Label("Create Cheat Sheet");
        titleLabel.getStyleClass().add("section-title");

        Label phraseLabel = new Label("PHRASE");
        phraseLabel.getStyleClass().add("label");
        TextField phraseField = new TextField();
        phraseField.setPromptText("e.g., Java Array Sort");
        phraseField.setPrefWidth(600);
        phraseField.setMaxWidth(600);

        Label variablesLabel = new Label("VARIABLES (comma separated)");
        variablesLabel.getStyleClass().add("label");
        TextField variablesField = new TextField();
        variablesField.setPromptText("e.g., java, array, sort");
        variablesField.setPrefWidth(600);
        variablesField.setMaxWidth(600);

        Label contentLabel = new Label("CONTENT");
        contentLabel.getStyleClass().add("label");
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Enter your code or notes here...");
        contentArea.setPrefHeight(350);
        contentArea.setPrefWidth(600);
        contentArea.setMaxWidth(600);
        contentArea.setWrapText(true);

        Button saveButton = new Button("Save Cheat Sheet");
        saveButton.setPrefWidth(200);
        saveButton.setOnAction(e -> {
            System.out.println("Save clicked");
        });

        VBox formContainer = new VBox(15);
        formContainer.getChildren().addAll(
            phraseLabel,
            phraseField,
            variablesLabel,
            variablesField,
            contentLabel,
            contentArea,
            saveButton
        );

        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(580);

        VBox layout = new VBox(25);
        layout.getStyleClass().add("content-area");
        layout.setPadding(new Insets(40));
        layout.getChildren().addAll(titleLabel, scrollPane);

        return layout;
    }
}