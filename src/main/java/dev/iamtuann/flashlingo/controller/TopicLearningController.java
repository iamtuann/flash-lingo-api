package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.TopicFlashcardDto;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.TopicFlashcardService;
import dev.iamtuann.flashlingo.service.TopicRecentService;
import dev.iamtuann.flashlingo.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/learning")
public class TopicLearningController {
    private final TopicFlashcardService topicFlashcardService;
    private final TopicRecentService topicRecentService;
    private final TopicService topicService;

    @GetMapping("/flashcard")
    public ResponseEntity<TopicFlashcardDto> getTopicFlashcard(
            @RequestParam Long topicId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        TopicFlashcardDto dto = topicFlashcardService.findByUserIdAndTopicId(userDetails.getId(), topicId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("flashcard")
    public ResponseEntity<?> updateTopicFlashcard(
            @RequestBody TopicFlashcardDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        topicFlashcardService.save(userDetails.getId(), request);
        topicRecentService.saveRecentTopic(request.getTopicId(), userDetails.getId());
        topicService.increaseLearned(request.getTopicId());
        return ResponseEntity.ok("");
    }
}
