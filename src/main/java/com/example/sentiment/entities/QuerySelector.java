package com.example.sentiment.entities;

public class QuerySelector {

    private String queryText;

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public QuerySelector(String queryText, String localDateTime) {

        this.queryText = queryText;
        this.localDateTime = localDateTime;
    }

    private String localDateTime;

}
