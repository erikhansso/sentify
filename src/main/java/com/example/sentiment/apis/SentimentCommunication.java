package com.example.sentiment.apis;

import java.io.*;
import java.net.*;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

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

    public List<Sentiment> getSentiment (Documents docs) {
        String text = new Gson().toJson(docs);

        byte[] encoded_text = new byte[0];
        try {
            encoded_text = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("The encoding of the JSON string to query Azure with failed");
        }

        URL url = null;
        try {
            url = new URL(host+path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("The URL to the Azure API was malformed");
        }
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to open connection to Azure");
        }
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
            System.out.println("An incorrect request method was used to send query to Azure");
        }
        connection.setRequestProperty("Content-Type", "text/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", azureKey);
        connection.setDoOutput(true);

        try {
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.write(encoded_text, 0, encoded_text.length);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Azure didn't accept the query");
        }

        StringBuilder response = new StringBuilder ();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("The handling of Azure\'s response failed");
        }

        SentimentResponse sentResp = new Gson().fromJson(response.toString(), SentimentResponse.class);

        return sentResp.getDocuments();
    }



}
