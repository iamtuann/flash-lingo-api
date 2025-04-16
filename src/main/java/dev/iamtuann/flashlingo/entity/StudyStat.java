package dev.iamtuann.flashlingo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "study_stat")
public class StudyStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "auth_user_id", nullable = false)
    private AuthUser authUser;

    @NotNull
    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    @Column(name = "total_duration_seconds")
    private Long totalDurationSeconds;

    @PrePersist
    public void prePersist() {
        this.statDate = LocalDate.now();
        this.totalDurationSeconds = 0L;
    }
}