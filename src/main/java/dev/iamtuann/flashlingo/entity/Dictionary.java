package dev.iamtuann.flashlingo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "dictionary")
@Getter
@Setter
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 255)
    @Column(name = "word")
    private String word;

    @Size(max = 255)
    @Column(name = "pronunciation")
    private String pronunciation;

    @Lob
    @Column(name = "definition")
    private String definition;

    @Lob
    @Column(name = "synonyms")
    private String synonyms;

    @Lob
    @Column(name = "antonyms")
    private String antonyms;

    public List<String> getSynonyms() {
        return convertStringToList(this.synonyms);
    }

    public List<String> getAntonyms() {
        return convertStringToList(this.antonyms);
    }

    private List<String> convertStringToList(String str) {
        if (str == null || str.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(str.split(","));
    }

}
