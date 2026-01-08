package com.syntaxcodex.application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SearchView {

    private VBox mainContainer;
    private Runnable onUpdate; // Callback to refresh the view
    private List<CheatSheet> allCheatSheets; // Cache all cheat sheets

    public SearchView() {
        this(null);
    }

    public SearchView(Runnable onUpdate) {
        this.onUpdate = onUpdate;
        this.allCheatSheets = new ArrayList<>();
    }

    public VBox createView() {
        mainContainer = new VBox(15);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(20));
        
        showSearchView();
        
        return mainContainer;
    }

    private void showSearchView() {
        mainContainer.getChildren().clear();
        
        // Top row: Search (75%) and Language filter (25%)
        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name, description, or content...");
        searchField.setStyle("-fx-background-color: #D2B48C; -fx-text-fill: #3E2723; -fx-prompt-text-fill: #6D4C41; " +
                            "-fx-border-color: #8B6F47; -fx-border-radius: 4px; -fx-background-radius: 4px; " +
                            "-fx-padding: 8px; -fx-font-size: 12px;");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchField.setMaxWidth(Double.MAX_VALUE);
        
        ComboBox<String> languageFilter = new ComboBox<>();
        languageFilter.setPromptText("All Languages");
        languageFilter.setPrefWidth(150);
        languageFilter.getItems().addAll(
            "All Languages",
            "Python", "C++", "Java", "JavaScript", "TypeScript", "Kotlin",
            "C#", "Ruby", "Go", "Rust", "PHP", "Swift", "SQL", "HTML", "CSS", "Other"
        );
        languageFilter.setValue("All Languages");
        
        topRow.getChildren().addAll(searchField, languageFilter);
        
        // Results container - no background, just transparent
        VBox resultsContainer = new VBox(10);
        resultsContainer.setPadding(new Insets(10));
        
        ScrollPane scrollPane = new ScrollPane(resultsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        
        // Style the scroll bar to be darker
        scrollPane.setStyle(
            "-fx-background: transparent; " +
            "-fx-background-color: transparent; " +
            "-fx-border-color: transparent;"
        );
        
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        mainContainer.getChildren().addAll(topRow, scrollPane);
        
        // Fetch cheat sheets once from Firebase
        allCheatSheets = fetchCheatSheetsFromFirebase();
        
        // Display initial results
        filterAndDisplayCheatSheets(resultsContainer, "", "All Languages");
        
        // Search and filter listeners - now just filter locally
        searchField.textProperty().addListener((obs, old, newVal) -> {
            filterAndDisplayCheatSheets(resultsContainer, newVal, languageFilter.getValue());
        });
        
        languageFilter.setOnAction(e -> {
            filterAndDisplayCheatSheets(resultsContainer, searchField.getText(), languageFilter.getValue());
        });
    }

    private void filterAndDisplayCheatSheets(VBox resultsContainer, String searchQuery, String languageFilter) {
        resultsContainer.getChildren().clear();
        
        // Filter from cached data instead of fetching from Firebase
        List<CheatSheet> filtered = new ArrayList<>();
        for (CheatSheet sheet : allCheatSheets) {
            boolean matchesSearch = searchQuery.isEmpty() || 
                                   sheet.name.toLowerCase().contains(searchQuery.toLowerCase()) ||
                                   sheet.description.toLowerCase().contains(searchQuery.toLowerCase()) ||
                                   sheet.content.toLowerCase().contains(searchQuery.toLowerCase());
            
            boolean matchesLanguage = languageFilter.equals("All Languages") || 
                                     sheet.language.equals(languageFilter);
            
            if (matchesSearch && matchesLanguage) {
                filtered.add(sheet);
            }
        }
        
        if (filtered.isEmpty()) {
            Label noResults = new Label("No cheat sheets found");
            noResults.setStyle("-fx-text-fill: #2C1810; -fx-font-size: 16px; -fx-font-weight: bold;");
            noResults.setPadding(new Insets(40, 0, 0, 0)); // Add top padding for visibility
            resultsContainer.getChildren().add(noResults);
        } else {
            for (CheatSheet sheet : filtered) {
                VBox card = createCheatSheetCard(sheet);
                resultsContainer.getChildren().add(card);
            }
        }
    }

    private VBox createCheatSheetCard(CheatSheet sheet) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: #C4A574; -fx-border-color: #8B6F47; " +
                     "-fx-border-radius: 4px; -fx-background-radius: 4px; -fx-border-width: 1px; " +
                     "-fx-cursor: hand;");
        
        // Title
        Label titleLabel = new Label(sheet.name);
        titleLabel.setStyle("-fx-text-fill: #2C1810; -fx-font-size: 14px; -fx-font-weight: bold;");
        
        // Language
        Label languageLabel = new Label("Language: " + sheet.language);
        languageLabel.setStyle("-fx-text-fill: #3E2723; -fx-font-size: 11px;");
        
        // Description
        Label descLabel = new Label(sheet.description);
        descLabel.setStyle("-fx-text-fill: #3E2723; -fx-font-size: 12px;");
        descLabel.setWrapText(true);
        
        card.getChildren().addAll(titleLabel, languageLabel, descLabel);
        
        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #B8935A; -fx-border-color: #8B6F47; " +
                                                  "-fx-border-radius: 4px; -fx-background-radius: 4px; -fx-border-width: 1px; " +
                                                  "-fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #C4A574; -fx-border-color: #8B6F47; " +
                                                 "-fx-border-radius: 4px; -fx-background-radius: 4px; -fx-border-width: 1px; " +
                                                 "-fx-cursor: hand;"));
        
        // Click to view/edit
        card.setOnMouseClicked(e -> showEditView(sheet));
        
        return card;
    }

    private void showEditView(CheatSheet sheet) {
        mainContainer.getChildren().clear();
        
        // Name field (75%) and Language dropdown (25%)
        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);
        
        TextField nameField = new TextField(sheet.name);
        nameField.setPromptText("Name");
        nameField.setStyle("-fx-background-color: #D2B48C; -fx-text-fill: #3E2723; -fx-prompt-text-fill: #6D4C41; " +
                          "-fx-border-color: #8B6F47; -fx-border-radius: 4px; -fx-background-radius: 4px; " +
                          "-fx-padding: 8px; -fx-font-size: 12px;");
        HBox.setHgrow(nameField, Priority.ALWAYS);
        nameField.setMaxWidth(Double.MAX_VALUE);
        
        ComboBox<String> languageDropdown = new ComboBox<>();
        languageDropdown.setValue(sheet.language);
        languageDropdown.setPrefWidth(150);
        languageDropdown.getItems().addAll(
            "Python", "C++", "Java", "JavaScript", "TypeScript", "Kotlin",
            "C#", "Ruby", "Go", "Rust", "PHP", "Swift", "SQL", "HTML", "CSS", "Other"
        );
        
        topRow.getChildren().addAll(nameField, languageDropdown);
        
        // Description area
        TextArea descriptionArea = new TextArea(sheet.description);
        descriptionArea.setPromptText("Description");
        descriptionArea.setPrefHeight(80);
        descriptionArea.setStyle("-fx-background-color: #D2B48C; -fx-text-fill: #3E2723; " +
                                "-fx-prompt-text-fill: #6D4C41; -fx-border-color: #8B6F47; " +
                                "-fx-border-radius: 4px; -fx-background-radius: 4px; " +
                                "-fx-padding: 8px; -fx-font-size: 12px; -fx-border-width: 1px;");
        
        // Content area
        TextArea contentArea = new TextArea(sheet.content);
        contentArea.setPromptText("Content / Code");
        contentArea.setStyle("-fx-background-color: #D2B48C; -fx-text-fill: #3E2723; " +
                            "-fx-prompt-text-fill: #6D4C41; -fx-border-color: #8B6F47; " +
                            "-fx-border-radius: 4px; -fx-background-radius: 4px; " +
                            "-fx-padding: 8px; -fx-font-size: 12px; " +
                            "-fx-font-family: 'Consolas', 'Monaco', monospace; -fx-border-width: 1px;");
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button saveButton = new Button("Save Changes");
        saveButton.setPrefWidth(150);
        saveButton.setStyle("-fx-background-color: #5C4033; -fx-text-fill: #D4AF37; -fx-font-size: 13px; " +
                           "-fx-font-weight: bold; -fx-padding: 10px 24px; -fx-border-radius: 4px; " +
                           "-fx-background-radius: 4px; -fx-cursor: hand;");
        
        Button deleteButton = new Button("Delete");
        deleteButton.setPrefWidth(150);
        deleteButton.setStyle("-fx-background-color: #5C4033; -fx-text-fill: #D4AF37; -fx-font-size: 13px; " +
                             "-fx-font-weight: bold; -fx-padding: 10px 24px; -fx-border-radius: 4px; " +
                             "-fx-background-radius: 4px; -fx-cursor: hand;");
        
        buttonBox.getChildren().addAll(saveButton, deleteButton);
        
        // Save button action
        saveButton.setOnAction(e -> {
            boolean success = updateCheatSheetInFirebase(
                sheet.id,
                nameField.getText(),
                languageDropdown.getValue(),
                descriptionArea.getText(),
                contentArea.getText()
            );
            
            if (success) {
                showSearchView(); // Reload search view
                if (onUpdate != null) onUpdate.run();
            }
        });
        
        // Delete button action
        deleteButton.setOnAction(e -> {
            boolean success = deleteCheatSheetFromFirebase(sheet.id);
            
            if (success) {
                showSearchView(); // Reload search view
                if (onUpdate != null) onUpdate.run();
            }
        });
        
        mainContainer.getChildren().addAll(topRow, descriptionArea, contentArea, buttonBox);
    }

    private List<CheatSheet> fetchCheatSheetsFromFirebase() {
        List<CheatSheet> sheets = new ArrayList<>();
        
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            String url = "https://firestore.googleapis.com/v1/projects/" + 
                        com.syntaxcodex.Config.getProjectId() + 
                        "/databases/(default)/documents/cheatsheets?key=" + 
                        com.syntaxcodex.Config.getWebApiKey();
            
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET();
            
            // Add auth token
            String authToken = com.syntaxcodex.FirebaseService.getAuthToken();
            if (authToken != null && !authToken.isEmpty()) {
                requestBuilder.header("Authorization", "Bearer " + authToken);
            }
            
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
                
                if (responseJson.has("documents")) {
                    JsonArray documents = responseJson.getAsJsonArray("documents");
                    
                    for (int i = 0; i < documents.size(); i++) {
                        JsonObject doc = documents.get(i).getAsJsonObject();
                        String docId = extractDocumentId(doc.get("name").getAsString());
                        JsonObject fields = doc.getAsJsonObject("fields");
                        
                        CheatSheet sheet = new CheatSheet();
                        sheet.id = docId;
                        sheet.name = fields.getAsJsonObject("name").get("stringValue").getAsString();
                        sheet.language = fields.getAsJsonObject("language").get("stringValue").getAsString();
                        sheet.description = fields.getAsJsonObject("description").get("stringValue").getAsString();
                        sheet.content = fields.getAsJsonObject("content").get("stringValue").getAsString();
                        
                        sheets.add(sheet);
                    }
                }
            } else {
                System.err.println("Failed to fetch cheat sheets. Status: " + response.statusCode() + ", Body: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error fetching cheat sheets: " + e.getMessage());
            e.printStackTrace();
        }
        
        return sheets;
    }

    private String extractDocumentId(String fullPath) {
        // Extract document ID from path like: projects/.../documents/cheatsheets/{id}
        String[] parts = fullPath.split("/");
        return parts[parts.length - 1];
    }

    private boolean updateCheatSheetInFirebase(String docId, String name, String language, String description, String content) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            // Build update document structure
            JsonObject fields = new JsonObject();
            
            JsonObject nameField = new JsonObject();
            nameField.addProperty("stringValue", name);
            fields.add("name", nameField);
            
            JsonObject languageField = new JsonObject();
            languageField.addProperty("stringValue", language);
            fields.add("language", languageField);
            
            JsonObject descriptionField = new JsonObject();
            descriptionField.addProperty("stringValue", description);
            fields.add("description", descriptionField);
            
            JsonObject contentField = new JsonObject();
            contentField.addProperty("stringValue", content);
            fields.add("content", contentField);
            
            JsonObject document = new JsonObject();
            document.add("fields", fields);
            
            String url = "https://firestore.googleapis.com/v1/projects/" + 
                        com.syntaxcodex.Config.getProjectId() + 
                        "/databases/(default)/documents/cheatsheets/" + docId + 
                        "?key=" + com.syntaxcodex.Config.getWebApiKey();
            
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(document.toString()));
            
            // Add auth token
            String authToken = com.syntaxcodex.FirebaseService.getAuthToken();
            if (authToken != null && !authToken.isEmpty()) {
                requestBuilder.header("Authorization", "Bearer " + authToken);
            }
            
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                System.out.println("Cheat sheet updated successfully!");
                return true;
            } else {
                System.out.println("Failed to update: " + response.body());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error updating cheat sheet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteCheatSheetFromFirebase(String docId) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            String url = "https://firestore.googleapis.com/v1/projects/" + 
                        com.syntaxcodex.Config.getProjectId() + 
                        "/databases/(default)/documents/cheatsheets/" + docId + 
                        "?key=" + com.syntaxcodex.Config.getWebApiKey();
            
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE();
            
            // Add auth token
            String authToken = com.syntaxcodex.FirebaseService.getAuthToken();
            if (authToken != null && !authToken.isEmpty()) {
                requestBuilder.header("Authorization", "Bearer " + authToken);
            }
            
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                System.out.println("Cheat sheet deleted successfully!");
                return true;
            } else {
                System.out.println("Failed to delete: " + response.body());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error deleting cheat sheet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Inner class to hold cheat sheet data
    private static class CheatSheet {
        String id;
        String name;
        String language;
        String description;
        String content;
    }
}