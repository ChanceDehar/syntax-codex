package com.syntaxcodex;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FirebaseService {

    private static final String AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";
    private static final String RESET_URL = "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=";

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
}