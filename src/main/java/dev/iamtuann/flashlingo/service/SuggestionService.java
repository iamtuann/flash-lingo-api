package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.PageDto;
import dev.iamtuann.flashlingo.model.PexelsPhotoDto;
import dev.iamtuann.flashlingo.model.WordDto;

import java.util.List;

public interface SuggestionService {
    List<WordDto> getSuggestWords(String prefix, Integer limit);

    List<String> getSuggestDefinitions(String word, String prefix, Integer limit);

    String getPronunciation(String word, String prefix);

    PageDto<PexelsPhotoDto> searchPhotos(String query, Integer page, Integer perPage);
}
