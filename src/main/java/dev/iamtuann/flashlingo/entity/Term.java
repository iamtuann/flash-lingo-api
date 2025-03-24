package dev.iamtuann.flashlingo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

}