package dev.iamtuann.flashlingo.service.impl;

import dev.iamtuann.flashlingo.entity.Dictionary;
import dev.iamtuann.flashlingo.model.WordDto;
import dev.iamtuann.flashlingo.repository.DictionaryRepository;
import dev.iamtuann.flashlingo.service.SuggestionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {
    private final DictionaryRepository dictionaryRepository;

    @Override
    public List<WordDto> getSuggestWords(String prefix, Integer limit) {
        List<Dictionary> dictionaries = dictionaryRepository.getSuggestWords(prefix, limit);
        return dictionaries.stream().map(WordDto::new).toList();
    }

    @Override
    public List<String> getSuggestDefinitions(String word, String prefix, Integer limit) {
        if (word == null || word.isEmpty()) {
            return new ArrayList<>();
        } else {
            String rawDefinition = dictionaryRepository.getDefinition(word);
            if (rawDefinition == null || rawDefinition.isEmpty()) {
                return new ArrayList<>();
            } else {
                return this.filterDefinitions(rawDefinition, prefix, limit);
            }
        }
    }

    List<String> filterDefinitions(String definition, String prefix, int limit) {
        return Arrays.stream(definition.split("\\.\\s*"))
                .filter(defn -> defn.contains(prefix))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
