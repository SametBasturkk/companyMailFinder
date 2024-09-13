package org.finder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LocalAIFilter {

    private static final String MODEL_NAME = "RichardErkhov/jpacifico_-_Chocolatine-3B-Instruct-DPO-Revised-gguf/Chocolatine-3B-Instruct-DPO-Revised.Q4_0.gguf";
    private static final String API_URL = "http://127.0.0.1:1234/v1/chat/completions";

    private static final String PROMPT_TEMPLATE = "%s \n Find the main website for the company %s based on the provided list of URLs.\n" +
            "Only provide the website URL without parameters or queries, just give me the domain. Don't write anything else.\n";

    private static HttpClient client = HttpClient.newHttpClient();

    public static String startAIProcess(String company, String urls) throws IOException, InterruptedException {
        String prompt = String.format(PROMPT_TEMPLATE, urls, company);
        return aiResponse(prompt);
    }

    private static String aiResponse(String prompt) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(buildRequestBody(prompt)))
                .build();

        try {
            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
            return resp.body().toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    private static String buildRequestBody(String prompt) {
        return String.format("{\n" +
                "    \"model\": \"%s\",\n" +
                "    \"messages\": [ \n" +
                "      { \"role\": \"user\", \"content\": \"%s\" }\n" +
                "    ], \n" +
                "    \"temperature\": 0.7, \n" +
                "    \"max_tokens\": -1,\n" +
                "    \"stream\": true\n" +
                "}", MODEL_NAME, prompt);
    }
}