package com.github.palicare.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.palicare.patient.PatientEntity;
import com.github.palicare.patient.PatientRepository;
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

@RestController
public class AiController {
    private final PatientRepository patientRepository;
    private final ObjectMapper objectMapper;

    record OllamaDTO(List<HashMap<String, String>> messages, long patientId) {
    }

    @Value("${stt.server.url}")
    private String sttServerUrl;

    @Value("${tts.server.url}")
    private String ttsServerUrl;

    @Value("${llm.server.url}")
    private String llmServerUrl;

    private final RestClient restClient;

    public AiController(PatientRepository patientRepository) {
        restClient = RestClient.create();
        this.patientRepository = patientRepository;
        this.objectMapper =  new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @PostMapping("/llm")
    public ResponseEntity<String> requestLlmResponse(@RequestBody OllamaDTO request) throws JsonProcessingException {
        StringBuilder prompt = new StringBuilder("Du bist ein machine learning model. Dir werden Fragen gestellt, die diesen Patienten betreffen. ");
        PatientEntity patient = patientRepository.findById(request.patientId).orElse(null);
        prompt.append(objectMapper.writeValueAsString(patient));
        prompt.append("Dein Vorheriger chat mit dem Nutzer ist:\n");
        for (HashMap<String, String> entry : request.messages) {
            prompt.append(entry.get("role")).append(":").append(entry.get("content")).append("\n");
        }
        prompt.append("Deine role ist assistant. Die Rolle des Nutzers ist user. Du musst eine Antwort auf deutsch schreiben.");
        var body = new HashMap<>();
        body.put("model", "gemma3:1b");
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
