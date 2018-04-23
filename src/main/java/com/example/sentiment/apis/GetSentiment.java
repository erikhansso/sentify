package com.example.sentiment.apis;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class sentimentQuery {

    public String id, language, text;

    public sentimentQuery(String id, String language, String text){
        this.id = id;
        this.language = language;
        this.text = text;
    }
}

class Documents {

    public List<sentimentQuery> documents;

    public Documents() {
        this.documents = new ArrayList<>();
    }



    public void add(String id, String language, String text) {
        this.documents.add (new sentimentQuery (id, language, text));
    }
}

public class GetSentiment {


    static String host = "https://northeurope.api.cognitive.microsoft.com";
    static String path = "/text/analytics/v2.0/sentiment";

    public static String getSentiment (String tweet, String azureKey) throws Exception {

        Documents documents = new Documents();
        documents.add("1", "sv", tweet);

        String text = new Gson().toJson(documents);

        byte[] encoded_text = text.getBytes("UTF-8");

        URL url = new URL(host+path);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", azureKey);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(encoded_text, 0, encoded_text.length);
        wr.flush();
        wr.close();

        StringBuilder response = new StringBuilder ();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        while((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    public static String prettify(String json_text) {

        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(json_text).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);
    }


}
