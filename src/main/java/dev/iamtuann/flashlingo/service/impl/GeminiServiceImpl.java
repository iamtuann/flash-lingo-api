package dev.iamtuann.flashlingo.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.iamtuann.flashlingo.service.GeminiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import java.util.List;

@Service
//@AllArgsConstructor
public class GeminiServiceImpl implements GeminiService {

    @Value("${gemini.api-key}")
    private String geminiApiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-lite:generateContent?key=";

    public List<String> generateExamples(String term, String definition) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody = """
            {
                "system_instruction": {
                    "parts": [
                        {
                            "text": "You are an AI assistant that generates English example sentences in JSON format without code Markdown."
                        }
                    ]
                },
                "contents": [
                    {
                        "parts": [
                            {
                                "text": "Generate exactly 3 short example sentences with simple words (8-16 words each) using the term '%s' in context.
                                         The term means: '%s'.
                                         The response must be a JSON array with exactly 3 string elements."
                            }
                        ]
                    }
                ],
                "generationConfig": {
                    "temperature": 0.7,
                    "maxOutputTokens": 100,
                    "topP": 0.9,
                    "topK": 5
                }
            }
            """.formatted(term, definition);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        String response = restTemplate.postForObject(GEMINI_API_URL + geminiApiKey, request, String.class);

        // Xử lý phản hồi JSON từ Gemini
        List<String> examples = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode candidatesNode = rootNode.path("candidates");

            if (candidatesNode.isArray() && !candidatesNode.isEmpty()) {
                String textResponse = candidatesNode.get(0).path("content").path("parts").get(0).path("text").asText();
                String cleanedJson = cleanJsonString(textResponse);
                examples = objectMapper.readValue(cleanedJson, List.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return examples;
    }

    private String cleanJsonString(String text) {
        return text.replaceAll("^```json\\s*", "").replaceAll("\\s*```$", "").trim();
    }
}
