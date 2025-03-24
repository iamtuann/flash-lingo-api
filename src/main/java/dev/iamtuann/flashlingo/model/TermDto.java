package dev.iamtuann.flashlingo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TermDto {
    private Long id;
    private String term;
    private String definition;
    private String pronunciation;
    private String partOfSpeech;
    private String example;
    private String level;
    private String imageUrl;
    private Integer rank;
    private Long topicId;
    private Date modifiedAt;
}
