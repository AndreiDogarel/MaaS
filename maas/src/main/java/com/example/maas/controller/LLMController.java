package com.example.maas.controller;

import com.example.maas.entities.LLMRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/llm")
public class LLMController {

    private final WebClient webClient;

    public LLMController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("LLM endpoint is working!");
    }

    @PostMapping("/ask")
    public ResponseEntity<String> askLLM(@RequestBody LLMRequest request) {

        Map<String, Object> payload = Map.of(
                "model", "openai/gpt-oss-20b",
                "stream", false,
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "You are a helpful assistant that helps people find information."
                        ),
                        Map.of(
                                "role", "user",
                                "content", request.getPrompt()
                        )
                )
        );

        String response = webClient.post()
                .uri("/v1/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // blocking ON PURPOSE (API sync)

        return ResponseEntity.ok(response);
    }
}
