package com.EmailWriter.Compose.Service;

import com.EmailWriter.Compose.Entity.EmailEntry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailReplyService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailReplyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateReply(EmailEntry entry) {
        String prompt = buildPrompt(entry);

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        String response = webClient.post()

                .uri(geminiApiUrl + geminiApiKey)

                .header("Content-Type", "application/json")

                .bodyValue(requestBody)

                .retrieve()

                .bodyToMono(String.class)

                .block();


        return extractResponse(response);
    }

    private String buildPrompt(EmailEntry entry) {
        StringBuilder prompt = new StringBuilder("Generate a professional Email reply for the following Email.");
        if (entry.getTone() != null && !entry.getTone().isEmpty()) {
            prompt.append(" Use a ").append(entry.getTone()).append(" tone.");
        }
        prompt.append("\nOriginal Email:\n").append(entry.getEmailcontent());
        return prompt.toString();
    }

    private String extractResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error processing response: " + e.getMessage();
        }
    }
}
