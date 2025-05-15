package dev.iamtuann.flashlingo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "term")
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "term", length = 500)
    private String term;

    @Column(name = "definition", length = 500)
    private String definition;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "ranking")
    private Integer rank;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(name = "modified_at")
    private Date modifiedAt = new Date();

    @Size(max = 255)
    @Column(name = "pronunciation")
    private String pronunciation;

    @Lob
    @Column(name = "example")
    private String example;

    @Size(max = 45)
    @Column(name = "level", length = 45)
    private String level;

    @Size(max = 45)
    @Column(name = "part_of_speech", length = 45)
    private String partOfSpeech;

    @Lob
    @Column(name = "synonyms")
    private String synonyms;

    @Lob
    @Column(name = "antonyms")
    private String antonyms;

    public List<String> getSynonyms() {
        return convertStringToList(this.synonyms);
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = String.join(",", synonyms);
    }

    public void setAntonyms(List<String> antonyms) {
        this.antonyms = String.join(",", antonyms);
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