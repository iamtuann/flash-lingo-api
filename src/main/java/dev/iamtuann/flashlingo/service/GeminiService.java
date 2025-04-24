package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.GenerateTopicResponse;

import javax.annotation.Nullable;
import java.util.List;

public interface GeminiService {

    List<String> generateExamples(String term, String definition);

    GenerateTopicResponse generateTopic(String topicName, int termCount, @Nullable String description, @Nullable String level);
}
