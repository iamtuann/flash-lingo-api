package dev.iamtuann.flashlingo.service;


import dev.iamtuann.flashlingo.model.StudyStatDto;

import java.time.LocalDate;
import java.util.List;

public interface StudyService {

    List<StudyStatDto> getStudyDailyTime(Long userId, LocalDate startDate, LocalDate endDate);

    void updateDailyTime(Long userId, long durationSeconds);
}
