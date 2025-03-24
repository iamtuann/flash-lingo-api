package dev.iamtuann.flashlingo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dictionary_new")
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

}
