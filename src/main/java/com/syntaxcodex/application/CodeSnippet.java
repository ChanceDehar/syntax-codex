package com.syntaxcodex.application;

import java.util.List;

public class CodeSnippet {

    private String id;
    private String phrase;
    private List<String> variables;
    private String content;
    private long timestamp;

    public CodeSnippet() {
    }

    public CodeSnippet(String phrase, List<String> variables, String content) {
        this.phrase = phrase;
        this.variables = variables;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
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
}