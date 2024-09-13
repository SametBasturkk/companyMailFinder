package org.finder;

import generator.RandomUserAgentGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.util.ArrayList;

public class GoogleUtil {

    public static HttpClient client = HttpClient.newHttpClient();


    public static String searchResults(String company) throws IOException, InterruptedException {
        return googleSearch(company);
    }

    private static String googleSearch(String company) throws IOException, InterruptedException {
        Document doc = Jsoup.connect("https://www.google.com/search?q=" +
                URLEncoder.encode(company, "UTF-8")).userAgent(RandomUserAgentGenerator.getNextNonMobile()).get();

        Elements results = doc.select("a[href]");
        ArrayList<String> urls = new ArrayList<>();
        for (var result : results) {
            urls.add(result.attr("href"));
        }

        for (String url : urls) {
            if (url.startsWith("/url?") && !url.contains("google")) {
                System.out.println(url.substring(7));
            }
        }

        LocalAIFilter.startAIProcess(company, urls.toString());

        return "test";
    }
}
