package dev.iamtuann.flashlingo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "topic")
public class Topic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

//    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auth_user_id", nullable = false)
    private AuthUser createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "slug")
    private String slug;

    @Column(name = "term_lang")
    private String termLang;

    @Column(name = "definition_lang")
    private String defLang;

    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("rank")
    private Set<Term> terms;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "topics")
    private Set<Folder> folders;

    @Lob
    @Column(name = "short_passage")
    private String shortPassage;

    @Lob
    @Column(name = "conversation")
    private String conversation;

    @ColumnDefault("0")
    @Column(name = "learn_count")
    private Integer learnCount;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.learnCount = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }
}