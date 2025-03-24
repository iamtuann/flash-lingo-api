package dev.iamtuann.flashlingo.model;

import dev.iamtuann.flashlingo.entity.Dictionary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WordDto {
    private Integer id;
    private String word;
    private String pronunciation;

    public WordDto(Dictionary dictionary) {
        this.id = dictionary.getId();
        this.word = dictionary.getWord();
        this.pronunciation = dictionary.getPronunciation();
    }
}
