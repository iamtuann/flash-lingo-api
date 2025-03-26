package dev.iamtuann.flashlingo.service.impl;

import com.google.cloud.texttospeech.v1.*;
import dev.iamtuann.flashlingo.service.TextToSpeechService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@Slf4j
public class GoogleTTSServiceImpl implements TextToSpeechService {

    private final TextToSpeechClient textToSpeechClient;

    @Override
    public byte[] synthesizeSpeech(String text) {
        try {
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
            return response.getAudioContent().toByteArray();
        } catch (Exception e) {
            log.error("Error in Text-to-Speech synthesis: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate speech");
        }
    }
}
