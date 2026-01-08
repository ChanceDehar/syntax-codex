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
        resultsLabel.setStyle("-fx-text-fill: #808080; -fx-font-size: 14px;");

        VBox resultsContainer = new VBox(15);
        resultsContainer.getChildren().add(resultsLabel);

        ScrollPane scrollPane = new ScrollPane(resultsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        VBox layout = new VBox(25);
        layout.getStyleClass().add("content-area");
        layout.setPadding(new Insets(40));
        layout.getChildren().addAll(titleLabel, searchField, scrollPane);

        return layout;
    }
}