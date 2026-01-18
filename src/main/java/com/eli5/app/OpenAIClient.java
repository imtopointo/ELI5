package com.eli5.app;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class OpenAIClient {

    private static final String OPENAI_URL = "https://api.openai.com/v1/responses";

    public static String ask(String prompt) {

        String apiKey = System.getenv("OPENAI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            return "ERROR: OPENAI_API_KEY is not set";
        }

        RestTemplate restTemplate = new RestTemplate();

        // ---- HEADERS ----
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // ---- BODY (CORRECT FORMAT) ----
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4.1-mini");

        // OpenAI Responses API expects input as an ARRAY
        body.put("input", new Object[] {
            Map.of(
                "role", "user",
                "content", new Object[] {
                    Map.of(
                        "type", "input_text",
                        "text", prompt
                    )
                }
            )
        });

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                OPENAI_URL,
                request,
                String.class
        );

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            return root
                    .get("output")
                    .get(0)
                    .get("content")
                    .get(0)
                    .get("text")
                    .asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing AI response";
        }
    }
}
