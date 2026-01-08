package com.syntaxcodex;

import com.syntaxcodex.FirebaseService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginScreen {

    private Stage stage;
    private Runnable onLoginSuccess;

    public LoginScreen(Stage stage, Runnable onLoginSuccess) {
        this.stage = stage;
        this.onLoginSuccess = onLoginSuccess;
    }

    public Scene createScene() {
        Button exitButton = new Button("×");
        exitButton.setStyle("-fx-background-color: #5C4033; -fx-text-fill: #D4AF37; -fx-font-size: 18px; " +
                           "-fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 4px 10px; " +
                           "-fx-border-radius: 3px; -fx-background-radius: 3px;");
        exitButton.setOnAction(e -> System.exit(0));

        HBox exitBox = new HBox();
        exitBox.setAlignment(Pos.TOP_RIGHT);
        exitBox.setPadding(new Insets(-17, -45, 10, 0));
        exitBox.getChildren().add(exitButton);

        Image titleImage = new Image(getClass().getResourceAsStream("/CodexTitle.png"));
        ImageView titleView = new ImageView(titleImage);
        titleView.setFitWidth(150);
        titleView.setPreserveRatio(true);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefWidth(200);
        emailField.setMaxWidth(200);
        emailField.setStyle("-fx-background-color: #D2B48C; -fx-text-fill: #3E2723; -fx-prompt-text-fill: #6D4C41; " +
                            "-fx-border-color: #8B6F47; -fx-border-radius: 4px; -fx-background-radius: 4px; " +
                            "-fx-padding: 8px; -fx-font-size: 12px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(200);
        passwordField.setMaxWidth(200);
        passwordField.setStyle("-fx-background-color: #D2B48C; -fx-text-fill: #3E2723; -fx-prompt-text-fill: #6D4C41; " +
                                "-fx-border-color: #8B6F47; -fx-border-radius: 4px; -fx-background-radius: 4px; " +
                                "-fx-padding: 8px; -fx-font-size: 12px;");

        TextField passwordVisible = new TextField();
        passwordVisible.setPromptText("Password");
        passwordVisible.setPrefWidth(200);
        passwordVisible.setMaxWidth(200);
        passwordVisible.setStyle("-fx-background-color: #D2B48C; -fx-text-fill: #3E2723; -fx-prompt-text-fill: #6D4C41; " +
                                "-fx-border-color: #8B6F47; -fx-border-radius: 4px; -fx-background-radius: 4px; " +
                                "-fx-padding: 8px; -fx-font-size: 12px;");
        passwordVisible.setManaged(false);
        passwordVisible.setVisible(false);

        passwordVisible.textProperty().bindBidirectional(passwordField.textProperty());

        Button togglePassword = new Button("⚊");
        togglePassword.setStyle("-fx-background-color: transparent; -fx-text-fill: #6D4C41; " +
                               "-fx-font-size: 14px; -fx-cursor: hand; -fx-padding: 0;");

        StackPane passwordStack = new StackPane();
        passwordStack.setMaxWidth(200);
        passwordStack.getChildren().addAll(passwordField, passwordVisible);
        StackPane.setAlignment(togglePassword, Pos.CENTER_RIGHT);
        StackPane.setMargin(togglePassword, new Insets(0, 8, 0, 0));
        passwordStack.getChildren().add(togglePassword);

        togglePassword.setOnAction(e -> {
            if (passwordField.isVisible()) {
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                passwordVisible.setVisible(true);
                passwordVisible.setManaged(true);
                togglePassword.setText("👁");
            } else {
                passwordVisible.setVisible(false);
                passwordVisible.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                togglePassword.setText("⚊");
            }
        });

        Button loginButton = new Button("Sign In");
        loginButton.setPrefWidth(200);
        loginButton.setMaxWidth(200);
        loginButton.setStyle("-fx-background-color: #5C4033; -fx-text-fill: #D4AF37; -fx-font-size: 14px; " +
                            "-fx-font-weight: bold; -fx-padding: 8px; -fx-border-radius: 4px; " +
                            "-fx-background-radius: 4px; -fx-cursor: hand;");

        Button forgotPasswordButton = new Button("Forgot password?");
        forgotPasswordButton.setPrefWidth(160);
        forgotPasswordButton.setMaxWidth(160);
        forgotPasswordButton.setStyle("-fx-background-color: #5C4033; -fx-text-fill: #D4AF37; " +
                                     "-fx-font-size: 10px; -fx-font-weight: 500; -fx-cursor: hand; " +
                                     "-fx-padding: 6px; -fx-border-radius: 4px; -fx-background-radius: 4px;");

        Label messageLabel = new Label();
        messageLabel.setVisible(false);
        messageLabel.setPrefWidth(200);
        messageLabel.setMaxWidth(200);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 10px; -fx-background-color: #fadbd8; " +
                             "-fx-background-radius: 3px; -fx-padding: 5px;");

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter email and password");
                messageLabel.setVisible(true);
                return;
            }

            boolean success = FirebaseService.login(email, password);
            if (success) {
                onLoginSuccess.run();
            } else {
                messageLabel.setText("Invalid email or password");
                messageLabel.setVisible(true);
            }
        });

        forgotPasswordButton.setOnAction(e -> {
            String email = emailField.getText();
            if (email.isEmpty()) {
                messageLabel.setText("Please enter your email");
                messageLabel.setVisible(true);
                return;
            }

            boolean success = FirebaseService.sendPasswordReset(email);
            if (success) {
                messageLabel.setText("Password reset email sent");
                messageLabel.setStyle("-fx-text-fill: #27ae60; -fx-background-color: #d5f4e6; " +
                                     "-fx-background-radius: 3px; -fx-padding: 5px; -fx-font-size: 10px;");
                messageLabel.setVisible(true);
            } else {
                messageLabel.setText("Failed to send reset email");
                messageLabel.setVisible(true);
            }
        });

        VBox loginBox = new VBox(6);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setMaxWidth(200);
        loginBox.setPadding(new Insets(-20, 0, 0, 0));
        loginBox.getChildren().addAll(
            exitBox,
            emailField,
            passwordStack,
            loginButton,
            forgotPasswordButton,
            messageLabel
        );

        Image backgroundImage = new Image(getClass().getResourceAsStream("/LoginBackground.png"));
        BackgroundImage bg = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(backgroundImage.getWidth() * 0.80, backgroundImage.getHeight() * 0.80, false, false, false, false)
        );

        StackPane root = new StackPane();
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

        loginBox.setOnMousePressed(event -> event.consume());
        loginBox.setOnMouseDragged(event -> event.consume());

        StackPane.setAlignment(titleView, Pos.TOP_CENTER);
        StackPane.setMargin(titleView, new Insets(230, 0, 0, 0));

        root.setBackground(new Background(bg));
        root.getChildren().addAll(titleView, loginBox);

        Scene scene = new Scene(root, backgroundImage.getWidth() * 0.80, backgroundImage.getHeight() * 0.80);
        scene.setFill(Color.TRANSPARENT);

        return scene;
    }
}