package dev.iamtuann.flashlingo.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Configuration
@Profile("prod")
public class GoogleCloudConfigProd {
    @Value("${GOOGLE_APPLICATION_CREDENTIALS:}")
    private String googleCredentialsJson;

    @Bean
    public TextToSpeechClient textToSpeechClient() throws IOException {
        GoogleCredentials credentials;
        if (StringUtils.hasText(googleCredentialsJson)) {
            credentials = GoogleCredentials
                    .fromStream(new ByteArrayInputStream(googleCredentialsJson.getBytes()))
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        } else {
            credentials = GoogleCredentials.getApplicationDefault().createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        }

        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        return TextToSpeechClient.create(settings);
    }
}
