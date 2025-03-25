package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.WordDto;

import java.util.List;

public interface SuggestionService {
    List<WordDto> getSuggestWords(String prefix, Integer limit);

    List<String> getSuggestDefinitions(String word, String prefix, Integer limit);

    String getPronunciation(String word, String prefix);
}
