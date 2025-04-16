package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.StudyStatDto;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.StudyService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/study")
@AllArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @GetMapping("daily-time")
    public ResponseEntity<?> getDailyStudyTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<StudyStatDto> stats = studyService.getStudyDailyTime(userDetails.getId(), start, end);
        return ResponseEntity.ok(stats);
    }

    @PostMapping("daily-time")
    public ResponseEntity<?> updateDailyStudyTime(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody Long durationSeconds
            ) {
        studyService.updateDailyTime(userDetails.getId(), durationSeconds);
        return ResponseEntity.ok().build();
    }
}
