package com.syntaxcodex;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.InputStream;

public class Config {

    public static void initializeFirebase() {
        try {
            InputStream serviceAccount = Config.class.getClassLoader()
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
}