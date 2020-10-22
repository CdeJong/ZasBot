package com.cdejong.rest;

import com.cdejong.Bot;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RestService {

    public static Optional<RedditResponse> getRedditResponse(String link) throws IOException {
        Bot.getLogger().warn("REQUEST");
        URL url = new URL(link);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-agent", "your bot 0.1");
        con.setRequestMethod("GET");

        con.getResponseCode();
        if (con.getResponseCode() != 200) {
            Bot.getLogger().info("not OK " + con.getResponseCode() );
            return Optional.empty();
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        Gson gson = new Gson();
        RedditResponse response = gson.fromJson(content.toString(), RedditResponse.class);

        con.disconnect();

        return Optional.ofNullable(response);
    }

//    public static CompletableFuture<Response> getRedditResponse2(String link) {
//        return //TODO
//    }
}
