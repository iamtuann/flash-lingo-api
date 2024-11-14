package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.TopicDto;
import dev.iamtuann.flashlingo.model.request.TopicRequest;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/topics")
@AllArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping("{id}")
    public ResponseEntity<TopicDto> getTopic(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TopicDto topicDto = topicService.findTopicById(id, userDetails != null ? userDetails.getId() : null);
        return ResponseEntity.ok(topicDto);
    }

    @PostMapping("")
    public ResponseEntity<TopicDto> saveTopic(@RequestBody TopicRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TopicDto topic = topicService.save(request, userDetails.getId());
        return new ResponseEntity<>(topic, HttpStatus.OK);
    }

    @PutMapping("{id}/change-status")
    public ResponseEntity<TopicDto> changeStatusTopic(@PathVariable Long id, @RequestParam Integer status, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TopicDto topic = topicService.changeStatus(id, status, userDetails.getId());
        return new ResponseEntity<>(topic, HttpStatus.OK);
    }
}
