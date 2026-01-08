package com.syntaxcodex.model;

import java.util.Arrays;
import java.util.List;

public class CheatSheet {
    
    private String id;
    private String title;
    private String description;
    private String language;
    private List<String> keywords;
    private String content;
    private long timestamp;

    public CheatSheet() {
    }

    public CheatSheet(String title, String description, String language, String keywords, String content) {
        this.title = title;
        this.description = description;
        this.language = language;
        this.keywords = parseKeywords(keywords);
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    private List<String> parseKeywords(String keywordsString) {
        if (keywordsString == null || keywordsString.trim().isEmpty()) {
            return Arrays.asList();
        }
        return Arrays.stream(keywordsString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CheatSheet{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", language='" + language + '\'' +
                ", keywords=" + keywords +
                '}';
    }
}