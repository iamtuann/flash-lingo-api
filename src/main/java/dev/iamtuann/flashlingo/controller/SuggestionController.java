package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.*;
import dev.iamtuann.flashlingo.model.request.GenerateTopicRequest;
import dev.iamtuann.flashlingo.service.GeminiService;
import dev.iamtuann.flashlingo.service.SuggestionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/suggestions")
@AllArgsConstructor
public class SuggestionController {

    private final SuggestionService suggestionService;
    private final GeminiService geminiService;

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
        String pronunciation = suggestionService.getPronunciation(word, prefix);
        Suggestion<String> suggestion = new Suggestion<>();
        suggestion.setWord(word);
        suggestion.setPrefix(prefix);
        suggestion.setSuggestions(pronunciation == null || pronunciation.isEmpty() ? Collections.emptyList() : Collections.singletonList(pronunciation));
        return ResponseEntity.ok(suggestion);
    }

    @PostMapping("/examples/generate")
    public ResponseEntity<List<String>> generateExamples(
            @RequestParam String term, @RequestParam String definition
    ) {
        List<String> examples = geminiService.generateExamples(term, definition);
        return ResponseEntity.ok(examples);
    }

    @GetMapping("/search-photos")
    public PageDto<PexelsPhotoDto> searchPhotos(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") Integer perPage) {
        return suggestionService.searchPhotos(query, page, perPage);
    }

    @PostMapping("topic/generate")
    public ResponseEntity<GenerateTopicResponse> generateTopic(
            @RequestBody @Valid GenerateTopicRequest request
    ) {
        GenerateTopicResponse response = geminiService.generateTopic(request.getName().toLowerCase(), request.getTermCount(), request.getDescription().toLowerCase(), request.getLevel().toLowerCase());
        return ResponseEntity.ok(response);
    }
}
