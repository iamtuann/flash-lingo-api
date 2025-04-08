package dev.iamtuann.flashlingo.service;

import java.util.List;

public interface GeminiService {

    List<String> generateExamples(String term, String definition);
}
