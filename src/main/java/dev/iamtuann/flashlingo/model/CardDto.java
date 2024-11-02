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
public class CardDto {
    private Long id;
    private String word;
    private String definition;
    private String imageUrl;
    private Integer rank;
    private Long topicId;
    private Date modifiedAt;
}
