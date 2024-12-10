package dev.iamtuann.flashlingo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "folder")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_user_id")
    private AuthUser createdBy;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @ColumnDefault("1")
    @Column(name = "status")
    private Integer status;

    @Column(name = "slug")
    private String slug;

    @ManyToMany
    @JoinTable(
            name = "folder_topic",
            joinColumns = {@JoinColumn(name = "folder_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "topic_id", referencedColumnName = "id")}
    )
    private Set<Topic> topics;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }
}