package com.syntaxcodex;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Config {

    private static String webApiKey;
    private static String projectId;

    public static void initializeFirebase() {
        try {
            InputStream serviceAccount = Config.class.getClassLoader()
                .getResourceAsStream("firebase-config.json");
            
            InputStreamReader reader = new InputStreamReader(serviceAccount);
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            webApiKey = json.get("apiKey").getAsString();
            projectId = json.get("projectId").getAsString();
            
            serviceAccount = Config.class.getClassLoader()
                .getResourceAsStream("firebase-config.json");
            
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
            
            FirebaseApp.initializeApp(options);
            
            System.out.println("Firebase initialized successfully");
        } catch (Exception e) {
            System.out.println("Failed to initialize Firebase: " + e.getMessage());
        }
    }

    public static String getWebApiKey() {
        return webApiKey;
    }

    public static String getProjectId() {
        return projectId;
    }
}