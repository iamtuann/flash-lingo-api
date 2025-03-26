package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.service.TextToSpeechService;
import lombok.AllArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tts")
@AllArgsConstructor
public class TTSController {
    private final TextToSpeechService ttsService;

    @GetMapping(value = "/speech", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generateSpeech(
            @RequestParam String text,
            @RequestParam(required = false) String lang
    ) {
        byte[] audioData = ttsService.synthesizeSpeech(text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("speech.mp3").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(audioData);
    }
}
