package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.TopicLearningDto;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.TopicLearningService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/learning")
public class TopicLearningController {
    private final TopicLearningService topicLearningService;

    @GetMapping("")
    public ResponseEntity<TopicLearningDto> getTopicLearning(
            @RequestParam Long topicId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        TopicLearningDto dto = topicLearningService.findByUserIdAndTopicId(userDetails.getId(), topicId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("")
    public ResponseEntity<?> updateTopicLearning(
            @RequestBody TopicLearningDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        topicLearningService.save(userDetails.getId(), request);
        return ResponseEntity.ok("");
    }
}
