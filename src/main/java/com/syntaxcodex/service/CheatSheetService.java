package com.syntaxcodex.service;

import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.syntaxcodex.model.CheatSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CheatSheetService {

    private static final String COLLECTION_USERS = "users";
    private static final String COLLECTION_CHEATSHEETS = "cheatsheets";

    private static Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public static boolean saveCheatSheet(String userId, CheatSheet cheatSheet) {
        try {
            Firestore db = getFirestore();
            
            Map<String, Object> data = new HashMap<>();
            data.put("title", cheatSheet.getTitle());
            data.put("description", cheatSheet.getDescription());
            data.put("language", cheatSheet.getLanguage());
            data.put("keywords", cheatSheet.getKeywords());
            data.put("content", cheatSheet.getContent());
            data.put("timestamp", cheatSheet.getTimestamp());

            DocumentReference docRef = db.collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_CHEATSHEETS)
                    .document();

            docRef.set(data).get();
            
            System.out.println("CheatSheet saved with ID: " + docRef.getId());
            return true;
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving cheat sheet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<CheatSheet> getAllCheatSheets(String userId) {
        List<CheatSheet> cheatSheets = new ArrayList<>();
        try {
            Firestore db = getFirestore();
            
            QuerySnapshot querySnapshot = db.collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_CHEATSHEETS)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .get();

            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                CheatSheet cheatSheet = document.toObject(CheatSheet.class);
                cheatSheet.setId(document.getId());
                cheatSheets.add(cheatSheet);
            }
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error getting cheat sheets: " + e.getMessage());
            e.printStackTrace();
        }
        return cheatSheets;
    }

    public static List<CheatSheet> searchCheatSheets(String userId, String searchQuery) {
        List<CheatSheet> allSheets = getAllCheatSheets(userId);
        List<CheatSheet> results = new ArrayList<>();
        
        String query = searchQuery.toLowerCase();
        
        for (CheatSheet sheet : allSheets) {
            if (sheet.getTitle().toLowerCase().contains(query) ||
                sheet.getDescription().toLowerCase().contains(query) ||
                sheet.getLanguage().toLowerCase().contains(query) ||
                sheet.getContent().toLowerCase().contains(query) ||
                sheet.getKeywords().stream().anyMatch(k -> k.toLowerCase().contains(query))) {
                results.add(sheet);
            }
        }
        
        return results;
    }

    public static boolean deleteCheatSheet(String userId, String cheatSheetId) {
        try {
            Firestore db = getFirestore();
            
            db.collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_CHEATSHEETS)
                    .document(cheatSheetId)
                    .delete()
                    .get();
            
            System.out.println("CheatSheet deleted: " + cheatSheetId);
            return true;
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error deleting cheat sheet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateCheatSheet(String userId, String cheatSheetId, CheatSheet cheatSheet) {
        try {
            Firestore db = getFirestore();
            
            Map<String, Object> data = new HashMap<>();
            data.put("title", cheatSheet.getTitle());
            data.put("description", cheatSheet.getDescription());
            data.put("language", cheatSheet.getLanguage());
            data.put("keywords", cheatSheet.getKeywords());
            data.put("content", cheatSheet.getContent());
            data.put("timestamp", cheatSheet.getTimestamp());

            db.collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_CHEATSHEETS)
                    .document(cheatSheetId)
                    .set(data)
                    .get();
            
            System.out.println("CheatSheet updated: " + cheatSheetId);
            return true;
            
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error updating cheat sheet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}