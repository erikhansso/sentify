package com.example.sentiment.apis;

import java.io.*;
import java.net.*;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

import com.example.sentiment.entities.SentimentQuery;
import com.example.sentiment.pojos.Sentiment;
import com.example.sentiment.pojos.SentimentResponse;
import com.google.gson.Gson;
import com.example.sentiment.pojos.Documents;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SentimentCommunication {


    @Value("${azureKey}")
    String azureKey;

    static String host = "https://northeurope.api.cognitive.microsoft.com";
    static String path = "/text/analytics/v2.0/sentiment";

    public List<Sentiment> getSentiment (Documents docs) throws Exception {


        String text = new Gson().toJson(docs);

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

        System.out.println("Naked response is: "+response.toString());
        SentimentResponse sentresp = new Gson().fromJson(response.toString(), SentimentResponse.class);
        System.out.println("This shows entire json-string : "+sentresp.toString());
        System.out.println("This shows score for first document: "+sentresp.getDocuments().get(0).getScore());


        return sentresp.getDocuments();
    }



}
