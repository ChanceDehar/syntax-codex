package com.syntaxcodex.application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class CreateView {

    private Runnable onSaveSuccess;

    public CreateView() {
        this(null);
    }

    public CreateView(Runnable onSaveSuccess) {
        this.onSaveSuccess = onSaveSuccess;
    }

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
        
        // Message label for validation/success messages
        Label messageLabel = new Label();
        messageLabel.setVisible(false);
        messageLabel.setPrefWidth(400);
        messageLabel.setMaxWidth(400);
        messageLabel.setAlignment(javafx.geometry.Pos.CENTER);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px; -fx-background-color: #fadbd8; " +
                             "-fx-background-radius: 4px; -fx-padding: 8px;");
        
        saveButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String language = languageDropdown.getValue();
            String description = descriptionArea.getText().trim();
            String content = contentArea.getText().trim();
            
            // Validation
            if (name.isEmpty()) {
                showError(messageLabel, "Name must be entered");
                nameField.requestFocus();
                return;
            }
            
            if (language == null || language.isEmpty()) {
                showError(messageLabel, "Language must be selected");
                languageDropdown.requestFocus();
                return;
            }
            
            if (description.isEmpty()) {
                showError(messageLabel, "Description must be entered");
                descriptionArea.requestFocus();
                return;
            }
            
            if (content.isEmpty()) {
                showError(messageLabel, "Content must be entered");
                contentArea.requestFocus();
                return;
            }
            
            // All validation passed - save to Firebase
            boolean success = saveToFirebase(name, language, description, content);
            
            if (success) {
                showSuccess(messageLabel, "Cheat sheet saved successfully!");
                // Clear fields after successful save
                nameField.clear();
                languageDropdown.setValue(null);
                descriptionArea.clear();
                contentArea.clear();
                
                // Switch to Search tab after 1 second
                if (onSaveSuccess != null) {
                    javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
                    pause.setOnFinished(ev -> onSaveSuccess.run());
                    pause.play();
                }
            } else {
                showError(messageLabel, "Failed to save cheat sheet. Please try again.");
            }
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
            buttonBox,
            messageLabel
        );

        return layout;
    }
    
    // Helper method to show error messages
    private void showError(Label messageLabel, String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 11px; -fx-background-color: #fadbd8; " +
                             "-fx-background-radius: 4px; -fx-padding: 8px;");
        messageLabel.setVisible(true);
    }
    
    // Helper method to show success messages
    private void showSuccess(Label messageLabel, String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 11px; -fx-background-color: #d5f4e6; " +
                             "-fx-background-radius: 4px; -fx-padding: 8px;");
        messageLabel.setVisible(true);
    }
    
    // Save to Firebase Firestore
    private boolean saveToFirebase(String name, String language, String description, String content) {
        return com.syntaxcodex.FirebaseService.saveCheatSheet(name, language, description, content);
    }
}