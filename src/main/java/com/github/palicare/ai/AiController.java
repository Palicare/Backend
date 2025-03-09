package com.github.palicare.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
public class AiController {
    record OllamaDTO(List<HashMap<String, String>> messages) {
    }

    @Value("${stt.server.url}")
    private String sttServerUrl;

    @Value("${tts.server.url}")
    private String ttsServerUrl;

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

    @PostMapping("/tts")
    public byte[] requestTTSResponse(@RequestBody String input) {
        // RestClient did not work for some reason
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("text", input);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<byte[]> response = restTemplate.postForEntity(ttsServerUrl, requestEntity, byte[].class);
        return response.getBody();
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
