package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.Suggestion;
import dev.iamtuann.flashlingo.model.WordDto;
import dev.iamtuann.flashlingo.service.SuggestionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/suggestions")
@AllArgsConstructor
public class SuggestionController {

    private final SuggestionService suggestionService;

    @GetMapping("/word")
    public ResponseEntity<Suggestion<WordDto>> getSuggestWords(
            @RequestParam(value = "prefix", defaultValue = "") String prefix,
            @RequestParam(value = "limit", defaultValue = "3") int limit
    ) {
        List<WordDto> words = suggestionService.getSuggestWords(prefix, limit);
        Suggestion<WordDto> suggestion = new Suggestion<>();
        suggestion.setSuggestions(words);
        suggestion.setPrefix(prefix);
        return ResponseEntity.ok(suggestion);
    }

    @GetMapping("/definition")
    public ResponseEntity<Suggestion<String>> getSuggestionDefinitions(
            @RequestParam(value = "word", defaultValue = "") String word,
            @RequestParam(value = "prefix", defaultValue = "") String prefix,
            @RequestParam(value = "limit", defaultValue = "3") int limit
    ) {
        List<String> definitions = suggestionService.getSuggestDefinitions(word, prefix, limit);
        Suggestion<String> suggestion = new Suggestion<>();
        suggestion.setWord(word);
        suggestion.setPrefix(prefix);
        suggestion.setSuggestions(definitions);
        return ResponseEntity.ok(suggestion);
    }

    @GetMapping("/pronunciation")
    public ResponseEntity<Suggestion<String>> getPronunciation(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "prefix", defaultValue = "") String prefix
    ) {
        String definitions = suggestionService.getPronunciation(word, prefix);
        Suggestion<String> suggestion = new Suggestion<>();
        suggestion.setWord(word);
        suggestion.setPrefix(prefix);
        suggestion.setSuggestions(Collections.singletonList(definitions));
        return ResponseEntity.ok(suggestion);
    }
}
