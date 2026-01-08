package com.syntaxcodex.application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class CreateView {

    public VBox createView() {
        // Top row: Name (75%) and Language (25%)
        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);
        
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setStyle("-fx-background-color: #D2B48C; -fx-text-fill: #3E2723; -fx-prompt-text-fill: #6D4C41; " +
                          "-fx-border-color: #8B6F47; -fx-border-radius: 4px; -fx-background-radius: 4px; " +
                          "-fx-padding: 8px; -fx-font-size: 12px;");
        HBox.setHgrow(nameField, Priority.ALWAYS);
        nameField.setMaxWidth(Double.MAX_VALUE);
        
        ComboBox<String> languageDropdown = new ComboBox<>();
        languageDropdown.setPromptText("Language");
        languageDropdown.setPrefWidth(150);
        languageDropdown.getItems().addAll(
            "Python", "C++", "Java", "JavaScript", "TypeScript", "Kotlin",
            "C#", "Ruby", "Go", "Rust", "PHP", "Swift", "SQL", "HTML", "CSS",
            "Other"
        );
        
        topRow.getChildren().addAll(nameField, languageDropdown);

        // Description box - exact same style as name field
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description");
        descriptionArea.setPrefHeight(80);
        descriptionArea.setStyle("-fx-background-color: #D2B48C; -fx-text-fill: #3E2723; -fx-prompt-text-fill: #6D4C41; " +
                                "-fx-border-color: #8B6F47; -fx-border-radius: 4px; -fx-background-radius: 4px; " +
                                "-fx-padding: 8px; -fx-font-size: 12px;");

        // Content box - exact same style as name field
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Content / Code");
        contentArea.setStyle("-fx-background-color: #D2B48C; -fx-text-fill: #3E2723; -fx-prompt-text-fill: #6D4C41; " +
                            "-fx-border-color: #8B6F47; -fx-border-radius: 4px; -fx-background-radius: 4px; " +
                            "-fx-padding: 8px; -fx-font-size: 12px; -fx-font-family: 'Consolas', 'Monaco', monospace;");
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        // Save button
        Button saveButton = new Button("Save Cheat Sheet");
        saveButton.setPrefWidth(200);
        saveButton.setStyle("-fx-background-color: #5C4033; -fx-text-fill: #D4AF37; -fx-font-size: 14px; " +
                           "-fx-font-weight: bold; -fx-padding: 10px 24px; -fx-border-radius: 4px; " +
                           "-fx-background-radius: 4px; -fx-cursor: hand;");
        saveButton.setOnAction(e -> {
            // TODO: Implement save functionality
            System.out.println("Save clicked:");
            System.out.println("Name: " + nameField.getText());
            System.out.println("Language: " + languageDropdown.getValue());
            System.out.println("Description: " + descriptionArea.getText());
            System.out.println("Content: " + contentArea.getText());
        });

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().add(saveButton);

        // Main layout
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
            topRow,
            descriptionArea,
            contentArea,
            buttonBox
        );

        return layout;
    }
}