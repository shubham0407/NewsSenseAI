package com.inshorts.newssense.ai.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class LLMService {

    private final RestTemplate restTemplate = new RestTemplate();

//    public String summarize(String content) {
//        try {
//            // Use HashMap instead of Map.of (Java 9+)
//            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("model", "phi"); // Or "mistral", etc.
//            requestBody.put("prompt", "Summarize in 2 sentences:\n\n" + content);
//            requestBody.put("stream", false); // Disable stream for now
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//            ResponseEntity<Map> response = restTemplate.exchange(
//                    "http://localhost:11434/api/generate",
//                    HttpMethod.POST,
//                    requestEntity,
//                    Map.class
//            );
//
//            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//                Object result = response.getBody().get("response");
//                return result != null ? result.toString().trim() : "[LLM Error: No response field]";
//            } else {
//                return "[LLM Error: Empty or invalid response]";
//            }
//
//        } catch (Exception e) {
//            return "[LLM Error: " + e.getMessage() + "]";
//        }
//    }

    public String summarize(String content) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("model", "phi");
                requestBody.put("prompt", "Summarize in 2 sentences:\n\n" + content);
                requestBody.put("stream", false);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

                ResponseEntity<Map> response = restTemplate.exchange(
                        "http://localhost:11434/api/generate",
                        HttpMethod.POST,
                        requestEntity,
                        Map.class
                );

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    Object result = response.getBody().get("response");
                    return result != null ? result.toString().trim() : "[LLM Error: No response field]";
                } else {
                    return "[LLM Error: Empty or invalid response]";
                }

            } catch (Exception e) {
                try {
                    Thread.sleep(1000L * attempt); // backoff
                } catch (InterruptedException ignored) {}
            }
        }
        return "[LLM Error: Failed after 3 attempts]";
    }

}
