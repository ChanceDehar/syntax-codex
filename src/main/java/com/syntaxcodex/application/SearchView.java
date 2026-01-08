package com.syntaxcodex.application;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SearchView {

    public VBox createView() {
        Label titleLabel = new Label("Search Cheat Sheets");
        titleLabel.getStyleClass().add("section-title");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by phrase or variables...");
        searchField.setPrefWidth(600);
        searchField.setMaxWidth(600);

        Label resultsLabel = new Label("Results will appear here");
        resultsLabel.setStyle("-fx-text-fill: #6D4C41; -fx-font-size: 12px; -fx-font-style: italic;");

        VBox resultsContainer = new VBox(15);
        resultsContainer.setPadding(new Insets(15));
        resultsContainer.getChildren().add(resultsLabel);

        ScrollPane scrollPane = new ScrollPane(resultsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(450);
        scrollPane.setStyle("-fx-background-color: #E8D4B0; -fx-border-color: #8B6F47; " +
                           "-fx-border-radius: 4px; -fx-background-radius: 4px;");

        VBox layout = new VBox(20);
        layout.getStyleClass().add("content-area");
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(titleLabel, searchField, scrollPane);

        return layout;
    }
}