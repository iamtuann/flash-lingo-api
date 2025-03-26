package dev.iamtuann.flashlingo.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Configuration
public class GoogleCloudConfig {
    @Value("${app.google.cloud.credentials}")
    private Resource googleCredentials;

    @Bean
    public TextToSpeechClient textToSpeechClient() throws IOException {
        GoogleCredentials credentials;
        if (googleCredentials.getFilename().startsWith("file:")) {
            credentials = GoogleCredentials.fromStream(new FileInputStream(googleCredentials.getFilename().substring(5)));
        } else {
            credentials = GoogleCredentials.fromStream(googleCredentials.getInputStream());
        }

        credentials.createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        return TextToSpeechClient.create(settings);
    }
}
