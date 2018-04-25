package com.example.sentiment.pojos;

import java.util.ArrayList;
import java.util.List;

public class Documents {

    public List<SentimentQuery> documents;

    public Documents() {
        this.documents = new ArrayList<>();
    }
    public void add(String id, String language, String text) {
        this.documents.add (new SentimentQuery(id, language, text));
    }


}
