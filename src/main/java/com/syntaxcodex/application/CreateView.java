package com.syntaxcodex.application;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CreateView {

    public VBox createView() {
        Label titleLabel = new Label("Create Cheat Sheet");
        titleLabel.getStyleClass().add("section-title");

        Label titleFieldLabel = new Label("TITLE");
        titleFieldLabel.getStyleClass().add("label");
        
        TextField titleField = new TextField();
        titleField.setPromptText("e.g., Java Array Sort");
        titleField.setPrefWidth(600);
        titleField.setMaxWidth(600);

        Label descriptionLabel = new Label("DESCRIPTION");
        descriptionLabel.getStyleClass().add("label");
        
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Brief explanation of what this does");
        descriptionField.setPrefWidth(600);
        descriptionField.setMaxWidth(600);

        Label languageLabel = new Label("LANGUAGE");
        languageLabel.getStyleClass().add("label");

        ComboBox<String> languageCombo = new ComboBox<>();
        languageCombo.getItems().addAll("Java", "Python", "JavaScript", "C++", "C#", "Ruby", "Go", "Rust", "PHP", "Swift", "Kotlin", "TypeScript", "Other");
        languageCombo.setPromptText("Select language");
        languageCombo.setPrefWidth(200);

        Label keywordsLabel = new Label("KEYWORDS (comma separated)");
        keywordsLabel.getStyleClass().add("label");
        
        TextField keywordsField = new TextField();
        keywordsField.setPromptText("e.g., java, array, sort, algorithm");
        keywordsField.setPrefWidth(600);
        keywordsField.setMaxWidth(600);

        Label contentLabel = new Label("CONTENT");
        contentLabel.getStyleClass().add("label");
        
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Enter your code snippet or notes here...");
        contentArea.setPrefHeight(200);
        contentArea.setPrefWidth(600);
        contentArea.setMaxWidth(600);
        contentArea.setWrapText(true);
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        Button saveButton = new Button("Save Cheat Sheet");
        saveButton.setPrefWidth(150);
        saveButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();
            String language = languageCombo.getValue();
            String keywords = keywordsField.getText().trim();
            String content = contentArea.getText().trim();

            if (title.isEmpty() || content.isEmpty()) {
                showMessage("Title and Content are required!", false);
                return;
            }

            if (language == null || language.isEmpty()) {
                showMessage("Please select a language!", false);
                return;
            }

            saveCheatSheet(title, description, language, keywords, content);
            
            titleField.clear();
            descriptionField.clear();
            languageCombo.setValue(null);
            keywordsField.clear();
            contentArea.clear();
            
            showMessage("Cheat sheet saved successfully!", true);
        });

        Button clearButton = new Button("Clear");
        clearButton.setPrefWidth(100);
        clearButton.setOnAction(e -> {
            titleField.clear();
            descriptionField.clear();
            languageCombo.setValue(null);
            keywordsField.clear();
            contentArea.clear();
        });

        HBox buttonBox = new HBox(12);
        buttonBox.getChildren().addAll(clearButton, saveButton);

        VBox formContainer = new VBox(12);
        formContainer.getChildren().addAll(
            titleFieldLabel,
            titleField,
            descriptionLabel,
            descriptionField,
            languageLabel,
            languageCombo,
            keywordsLabel,
            keywordsField,
            contentLabel,
            contentArea,
            buttonBox
        );

        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(480);
        scrollPane.setStyle("-fx-background: transparent; -fx-border-color: transparent;");

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, scrollPane);

        return layout;
    }

    private void saveCheatSheet(String title, String description, String language, String keywords, String content) {
        System.out.println("Saving cheat sheet:");
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Language: " + language);
        System.out.println("Keywords: " + keywords);
        System.out.println("Content: " + content);
    }

    private void showMessage(String message, boolean success) {
        System.out.println((success ? "SUCCESS: " : "ERROR: ") + message);
    }
}