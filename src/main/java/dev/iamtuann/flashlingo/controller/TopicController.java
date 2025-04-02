package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.PageDto;
import dev.iamtuann.flashlingo.model.TopicDto;
import dev.iamtuann.flashlingo.model.request.TopicRequest;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.TopicService;
import dev.iamtuann.flashlingo.utils.PageUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/topics")
@AllArgsConstructor
public class TopicController {
    private final TopicService topicService;
    private final PageUtil pageUtil;

    @GetMapping("")
    public ResponseEntity<PageDto<TopicDto>> getTopics(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Pageable pageable = pageUtil.getPageable(pageIndex, pageSize, key, orderBy);
        PageDto<TopicDto> topics = topicService.searchTopics(name, null, null, userDetails.getId(), pageable);
        return ResponseEntity.ok(topics);
    }

    @GetMapping("list/popular")
    public ResponseEntity<PageDto<TopicDto>> getPopularTopics() {
        Pageable pageable = pageUtil.getPageable(1, 10, "learnCount", "desc");
        PageDto<TopicDto> topics = topicService.searchTopics("", null, null, null, pageable);
        return ResponseEntity.ok(topics);
    }

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

    @PutMapping("{id}/status")
    public ResponseEntity<TopicDto> changeStatusTopic(@PathVariable Long id, @RequestParam Integer status, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TopicDto topic = topicService.changeStatus(id, status, userDetails.getId());
        return new ResponseEntity<>(topic, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTopic(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        topicService.deleteTopic(id, userDetails.getId());
        return ResponseEntity.ok("Deleted topic successfully");
    }
}
