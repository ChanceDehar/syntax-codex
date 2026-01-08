package com.syntaxcodex;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

public class FirebaseService {

    private static final String AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";
    private static final String RESET_URL = "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=";
    private static final String FIRESTORE_URL = "https://firestore.googleapis.com/v1/projects/";
    
    private static String authToken; // Store the auth token

    public static boolean login(String email, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            JsonObject json = new JsonObject();
            json.addProperty("email", email);
            json.addProperty("password", password);
            json.addProperty("returnSecureToken", true);
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AUTH_URL + Config.getWebApiKey()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                // Parse response to get the idToken
                JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
                authToken = responseJson.get("idToken").getAsString();
                System.out.println("Login successful for: " + email);
                return true;
            } else {
                System.out.println("Login failed: " + response.body());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    public static boolean sendPasswordReset(String email) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            JsonObject json = new JsonObject();
            json.addProperty("requestType", "PASSWORD_RESET");
            json.addProperty("email", email);
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RESET_URL + Config.getWebApiKey()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                System.out.println("Password reset email sent to: " + email);
                return true;
            } else {
                System.out.println("Failed to send reset email: " + response.body());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Reset email error: " + e.getMessage());
            return false;
        }
    }

    public static boolean saveCheatSheet(String name, String language, String description, String content) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            // Build Firestore document structure
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
            
            JsonObject createdAtField = new JsonObject();
            createdAtField.addProperty("timestampValue", Instant.now().toString());
            fields.add("createdAt", createdAtField);
            
            JsonObject document = new JsonObject();
            document.add("fields", fields);
            
            String firestoreUrl = FIRESTORE_URL + Config.getProjectId() + "/databases/(default)/documents/cheatsheets?key=" + Config.getWebApiKey();
            
            // Build request with Authorization header
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(firestoreUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(document.toString()));
            
            // Add auth token if available
            if (authToken != null && !authToken.isEmpty()) {
                requestBuilder.header("Authorization", "Bearer " + authToken);
            }
            
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                System.out.println("Cheat sheet saved successfully!");
                return true;
            } else {
                System.out.println("Failed to save cheat sheet: " + response.body());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Save error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Getter for auth token
    public static String getAuthToken() {
        return authToken;
    }
}