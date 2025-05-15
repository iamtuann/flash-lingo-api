package dev.iamtuann.flashlingo.service.impl;

import dev.iamtuann.flashlingo.entity.Dictionary;
import dev.iamtuann.flashlingo.model.*;
import dev.iamtuann.flashlingo.repository.DictionaryRepository;
import dev.iamtuann.flashlingo.service.SuggestionService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
//@AllArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {
    private final DictionaryRepository dictionaryRepository;

    private final RestTemplate restTemplate;

    private static final String DATAMUSE_API = "https://api.datamuse.com/words";
    private static final int MAX_RESULTS = 10;

    @Value("${app.pexels.api-key}")
    private String pexelsApiKey;

    public SuggestionServiceImpl(DictionaryRepository dictionaryRepository, RestTemplate restTemplate) {
        this.dictionaryRepository = dictionaryRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<WordDto> getSuggestWords(String prefix, Integer limit) {
        List<Dictionary> dictionaries = dictionaryRepository.getSuggestWords(prefix.toLowerCase(), limit);
        return dictionaries.stream().map(WordDto::new).toList();
    }

    @Override
    public List<String> getSuggestDefinitions(String word, String prefix, Integer limit) {
        if (word == null || word.isEmpty()) {
            return new ArrayList<>();
        } else {
            String rawDefinition = dictionaryRepository.getDefinition(word.toLowerCase());
            if (rawDefinition == null || rawDefinition.isEmpty()) {
                return new ArrayList<>();
            } else {
                return this.filterDefinitions(rawDefinition.toLowerCase(), prefix.toLowerCase(), limit);
            }
        }
    }

    @Override
    public String getPronunciation(String word, String prefix) {
        if (word == null || word.isEmpty()) {
            return "";
        } else {
            return dictionaryRepository.getPronunciation(word.toLowerCase());
        }
    }

    List<String> filterDefinitions(String definition, String prefix, int limit) {
        return Arrays.stream(definition.split("\\.\\s*"))
                .filter(defn -> defn.contains(prefix))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "photos")
    public PageDto<PexelsPhotoDto> searchPhotos(String query, Integer page, Integer perPage) {
        // Build URL with query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.pexels.com/v1/search")
                .queryParam("query", query)
                .queryParam("page", page)
                .queryParam("per_page", perPage);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", pexelsApiKey);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the request
        ResponseEntity<PexelsResponse> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                PexelsResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            PexelsResponse pexels = response.getBody();
            PageDto<PexelsPhotoDto> pageDto = new PageDto<>();
            pageDto.setTotalPages(pexels.getTotal_results() / pexels.getPer_page());
            pageDto.setTotalElements(pexels.getTotal_results());
            List<PexelsPhotoDto> content = pexels.getPhotos().stream().map(PexelsPhotoDto::new).toList();
            pageDto.setContent(content);
            return pageDto;
        } else {
            throw new RuntimeException("Failed to fetch photos from Pexels: " + response.getStatusCode());
        }
    }

    @Override
    public WordThesaurus getWordThesaurus(String word) {
        Optional<Dictionary> dictionary = dictionaryRepository.findByWord(word.toLowerCase());
        if (dictionary.isEmpty()) {
            return new WordThesaurus(word, Collections.emptyList(), Collections.emptyList());
        }
        WordThesaurus thesaurus = new WordThesaurus();
        thesaurus.setWord(word);
        Dictionary d = dictionary.get();
        if (d.getSynonyms() == null || d.getSynonyms().isEmpty()) {
            d = crawSynonyms(d);
        }
        if (d.getAntonyms() == null || d.getAntonyms().isEmpty()) {
            d = crawAntonyms(d);
        }
        thesaurus.setSynonyms(d.getSynonyms());
        thesaurus.setAntonyms(d.getAntonyms());
        return thesaurus;
    }

    private Dictionary crawSynonyms(Dictionary dictionary) {
        List<DatamuseWord> synonymWords = fetchFromDatamuse("rel_syn", dictionary.getWord().toLowerCase());
        String synonyms = synonymWords.stream().map(DatamuseWord::getWord).collect(Collectors.joining(","));
        dictionary.setSynonyms(synonyms);
        return dictionaryRepository.save(dictionary);
    }

    private Dictionary crawAntonyms(Dictionary dictionary) {
        List<DatamuseWord> antonymWords = fetchFromDatamuse("rel_ant", dictionary.getWord().toLowerCase());
        String antonyms = antonymWords.stream().map(DatamuseWord::getWord).collect(Collectors.joining(","));
        dictionary.setAntonyms(antonyms);
        return dictionaryRepository.save(dictionary);
    }

    private List<DatamuseWord> fetchFromDatamuse(String relation, String word) {
        String url = String.format("%s?%s=%s&max=%d", DATAMUSE_API, relation, word, MAX_RESULTS);

        try {
            ResponseEntity<DatamuseWord[]> response = restTemplate.getForEntity(url, DatamuseWord[].class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


    @Data
    private static class DatamuseWord {
        private String word;
        private int score;
    }
}
