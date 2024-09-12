package org.finder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GoogleUtil {

    public static HttpClient client = HttpClient.newHttpClient();


    public static String searchResults(String company) throws IOException, InterruptedException {
        return googleSearch(company);
    }

    private static String googleSearch(String company) throws IOException, InterruptedException {
        String url = "https://www.google.com/search?q=" + URLEncoder.encode(company, "UTF-8");
        HttpResponse resp = client.send(HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build(), HttpResponse.BodyHandlers.ofString());

        return resp.body().toString();

    }
}
