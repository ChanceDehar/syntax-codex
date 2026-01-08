package com.syntaxcodex;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {

    private Stage stage;
    private Runnable onLoginSuccess;

    public LoginScreen(Stage stage, Runnable onLoginSuccess) {
        this.stage = stage;
        this.onLoginSuccess = onLoginSuccess;
    }

    public Scene createScene() {
        Image banner = new Image(getClass().getResourceAsStream("/CodexBanner.png"));
        ImageView bannerView = new ImageView(banner);
        bannerView.setFitWidth(300);
        bannerView.setPreserveRatio(true);

        Label emailLabel = new Label("EMAIL");
        TextField emailField = new TextField();
        emailField.setPromptText("you@example.com");
        emailField.setPrefWidth(300);

        Label passwordLabel = new Label("PASSWORD");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(300);

        Button loginButton = new Button("Sign In");
        loginButton.setPrefWidth(300);

        Button forgotPasswordButton = new Button("Forgot password?");
        forgotPasswordButton.getStyleClass().add("forgot-button");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");
        messageLabel.setVisible(false);
        messageLabel.setPrefWidth(300);
        messageLabel.setAlignment(Pos.CENTER);

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter email and password");
                messageLabel.getStyleClass().remove("message-label-success");
                messageLabel.setVisible(true);
                return;
            }

            boolean success = FirebaseService.login(email, password);
            if (success) {
                onLoginSuccess.run();
            } else {
                messageLabel.setText("Invalid email or password");
                messageLabel.getStyleClass().remove("message-label-success");
                messageLabel.setVisible(true);
            }
        });

        forgotPasswordButton.setOnAction(e -> {
            String email = emailField.getText();
            if (email.isEmpty()) {
                messageLabel.setText("Please enter your email");
                messageLabel.getStyleClass().remove("message-label-success");
                messageLabel.setVisible(true);
                return;
            }

            boolean success = FirebaseService.sendPasswordReset(email);
            if (success) {
                messageLabel.setText("Password reset email sent");
                messageLabel.getStyleClass().add("message-label-success");
                messageLabel.setVisible(true);
            } else {
                messageLabel.setText("Failed to send reset email");
                messageLabel.getStyleClass().remove("message-label-success");
                messageLabel.setVisible(true);
            }
        });

        VBox container = new VBox(18);
        container.setPadding(new Insets(36, 32, 36, 32));
        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("login-container");
        container.setMaxWidth(380);
        container.getChildren().addAll(
            bannerView,
            emailLabel,
            emailField,
            passwordLabel,
            passwordField,
            loginButton,
            forgotPasswordButton,
            messageLabel
        );

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(container);

        Scene scene = new Scene(root, 520, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return scene;
    }
}