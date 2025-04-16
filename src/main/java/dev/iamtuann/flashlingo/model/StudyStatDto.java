package dev.iamtuann.flashlingo.model;

import dev.iamtuann.flashlingo.entity.StudyStat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyStatDto {
    private Long id;
    private Long userId;
    private LocalDate statDate;
    private Long totalDurationSeconds;
    private Integer sessionCount;

    public StudyStatDto(StudyStat studyStat) {
        this.id = studyStat.getId();
        this.userId = studyStat.getAuthUser().getId();
        this.statDate = studyStat.getStatDate();
        this.totalDurationSeconds = studyStat.getTotalDurationSeconds();
    }
}
