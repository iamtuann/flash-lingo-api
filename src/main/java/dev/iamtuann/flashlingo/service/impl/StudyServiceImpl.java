package dev.iamtuann.flashlingo.service.impl;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.entity.StudyStat;
import dev.iamtuann.flashlingo.exception.APIException;
import dev.iamtuann.flashlingo.model.StudyStatDto;
import dev.iamtuann.flashlingo.repository.AuthUserRepository;
import dev.iamtuann.flashlingo.repository.StudyStatRepository;
import dev.iamtuann.flashlingo.service.StudyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudyServiceImpl implements StudyService {
    private final StudyStatRepository studyStatRepository;
    private final AuthUserRepository authUserRepository;

    @Override
    public List<StudyStatDto> getStudyDailyTime(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new APIException(HttpStatus.BAD_REQUEST ,"Start date cannot be after end date");
        }
        List<StudyStat> stats = studyStatRepository.findByAuthUserIdAndStatDateBetween(userId, startDate, endDate);

        return stats.stream().map(StudyStatDto::new).collect(Collectors.toList());
    }

    @Override
    public void updateDailyTime(Long userId, long durationSeconds) {
        AuthUser user = authUserRepository.findAuthUserById(userId);
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        LocalDateTime midnight = today.atStartOfDay();
        long secondsSinceMidnight = Duration.between(midnight, now).getSeconds();

        StudyStat todayStat;
//                = studyStatRepository.findByAuthUserIdAndStatDate(userId, today)
//                .orElseGet(() -> {
//                    StudyStat newStat = new StudyStat();
//                    newStat.setAuthUser(user);
//                    newStat.setStatDate(today);
//                    newStat.setTotalDurationSeconds(0L);
//                    return newStat;
//                });
        Optional<StudyStat> optional = studyStatRepository.findByAuthUserIdAndStatDate(userId, today);
        if (optional.isPresent()) {
            todayStat = optional.get();
        } else {
            todayStat = new StudyStat();
            todayStat.setAuthUser(user);
            todayStat.setStatDate(today);
            todayStat.setTotalDurationSeconds(0L);
        }

        LocalDate yesterday = today.minusDays(1);

        if (durationSeconds > secondsSinceMidnight) {
            long overflowSeconds = durationSeconds - secondsSinceMidnight;

            todayStat.setTotalDurationSeconds(secondsSinceMidnight);
            studyStatRepository.save(todayStat);

            StudyStat yesterdayStat = studyStatRepository.findByAuthUserIdAndStatDate(userId, yesterday)
                    .orElse(null);
            if (yesterdayStat != null) {
                yesterdayStat.setTotalDurationSeconds(yesterdayStat.getTotalDurationSeconds() + overflowSeconds);
                studyStatRepository.save(yesterdayStat);
            } else {
                StudyStat newYesterdayStat = new StudyStat();
                newYesterdayStat.setAuthUser(user);
                newYesterdayStat.setStatDate(yesterday);
                newYesterdayStat.setTotalDurationSeconds(overflowSeconds);
                studyStatRepository.save(newYesterdayStat);
            }
        } else {
            todayStat.setTotalDurationSeconds(todayStat.getTotalDurationSeconds() + durationSeconds);
            studyStatRepository.save(todayStat);
        }
    }
}
