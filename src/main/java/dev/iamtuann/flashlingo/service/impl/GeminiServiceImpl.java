package dev.iamtuann.flashlingo.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.iamtuann.flashlingo.model.GenerateTopicResponse;
import dev.iamtuann.flashlingo.service.GeminiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nullable;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    @Cacheable(value = "gen-topics")
    public GenerateTopicResponse generateTopic(String topicName, int termCount, @Nullable String description, @Nullable String level) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        // Tạo Prompt bằng String bình thường
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Generate a vocabulary topic with the following details:\n")
                .append("- Topic name: \"").append(topicName).append("\"\n")
                .append("- Number of terms: ").append(termCount).append("\n");
        if (description != null && !description.isBlank()) {
            promptBuilder.append("- Description (optional): \"").append(description).append("\"\n");
        }
        if (level != null && !level.isBlank()) {
            promptBuilder.append("- Vocabulary level: ").append(level).append("\n");
        }
        promptBuilder.append("\nReturn the result strictly in JSON format, with the following structure:\n")
                .append("- name: same as input.\n")
                .append("- description: if provided.\n")
                .append("- terms: array of exactly ")
                .append(termCount)
                .append(" items. Each item has:\n")
                .append("    - term: English word or phrase.\n")
                .append("    - definition: short Vietnamese meaning.\n")
                .append("    - pronunciation: IPA style.\n")
                .append("    - example: short sentence (8–16 words).\n")
                .append("- shortPassage: a short paragraph using some of the terms above, written smoothly and naturally like a travel story or daily moment. Keep it clear and engaging. Do not use markdown.\n")
                .append("\nStrictly return JSON. Do not use markdown (no ```json).");

        String prompt = promptBuilder.toString();

        // Sử dụng Map thay vì string thủ công
        Map<String, Object> body = new HashMap<>();

        Map<String, Object> systemInstruction = Map.of(
                "parts", List.of(Map.of("text", "You are an AI assistant that generates structured vocabulary learning topics in JSON format. Your output must strictly follow the JSON format so it can be parsed by code. Avoid using markdown or extra commentary."))
        );

        Map<String, Object> contents = Map.of(
                "parts", List.of(Map.of("text", prompt))
        );

        Map<String, Object> generationConfig = Map.of(
                "temperature", 0.7,
//                "maxOutputTokens", 800,
                "topP", 0.9,
                "topK", 5
        );

        body.put("system_instruction", systemInstruction);
        body.put("contents", List.of(contents));
        body.put("generationConfig", generationConfig);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);
            String response = restTemplate.postForObject(GEMINI_API_URL + geminiApiKey, request, String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode textNode = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");

            String cleanedJson = cleanJsonString(textNode.asText());

            return objectMapper.readValue(cleanedJson, GenerateTopicResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String cleanJsonString(String text) {
        return text.replaceAll("^```json\\s*", "").replaceAll("\\s*```$", "").trim();
    }
}
