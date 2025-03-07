package com.github.palicare.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
public class AiController {
    record OllamaDTO(List<HashMap<String, String>> messages) {
    }

    @Value("${stt.server.url}")
    private String sttServerUrl;

    @Value("${llm.server.url}")
    private String llmServerUrl;

    private final RestClient restClient;

    public AiController() {
        restClient = RestClient.create();
    }

    @PostMapping("/llm")
    public ResponseEntity<String> requestLlmResponse(@RequestBody OllamaDTO request) {
        StringBuilder prompt = new StringBuilder("You are a machine learning model. Your previous chat with the user is this:\n");
        for (HashMap<String, String> entry : request.messages) {
            prompt.append(entry.get("role")).append(":").append(entry.get("content")).append("\n");
        }
        prompt.append("Your role is assistant. The users role is user. You need to write a response.");

        var body = new HashMap<>();
        body.put("model", "deepseek-r1:1.5b");
        body.put("prompt", prompt.toString());
        body.put("stream", false);
        var response = restClient.post().uri(llmServerUrl + "/api/generate").body(body).retrieve();
        var responseBody = response.body(HashMap.class);
        if (responseBody != null && responseBody.containsKey("response")) {
            return new ResponseEntity<>(responseBody.get("response").toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/stt")
    public ResponseEntity<String> requestSttResponse(@RequestParam MultipartFile file) throws IOException {
        ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename(); // Name der Datei beibehalten
            }
        };

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", fileResource);

        var responseBody = restClient.post()
                .uri(sttServerUrl)
                .body(parts)
                .retrieve()
                .body(HashMap.class);

        if (responseBody != null && responseBody.containsKey("transcription")) {
            return new ResponseEntity<>(responseBody.get("transcription").toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
