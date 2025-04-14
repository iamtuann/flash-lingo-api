package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.FolderDto;
import dev.iamtuann.flashlingo.model.PageDto;
import dev.iamtuann.flashlingo.model.TopicDto;
import dev.iamtuann.flashlingo.model.request.AddTopicRequest;
import dev.iamtuann.flashlingo.model.request.FolderRequest;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.FolderService;
import dev.iamtuann.flashlingo.service.TopicService;
import dev.iamtuann.flashlingo.utils.PageUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/folders")
@AllArgsConstructor
public class FolderController {
    private final FolderService folderService;
    private final PageUtil pageUtil;
    private final TopicService topicService;

    @GetMapping("{id}")
    public ResponseEntity<FolderDto> getFolder(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FolderDto folderDto = folderService.findFolderById(id, userDetails != null ? userDetails.getId() : null);
        return ResponseEntity.ok(folderDto);
    }

    @PostMapping("")
    public ResponseEntity<FolderDto> createFolder(@Valid @RequestBody FolderRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FolderDto FolderDto = folderService.create(request, userDetails.getId());
        return new ResponseEntity<>(FolderDto, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<FolderDto> updateFolder(@PathVariable Long id, @RequestBody FolderRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FolderDto FolderDto = folderService.update(id, request, userDetails.getId());
        return new ResponseEntity<>(FolderDto, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        folderService.delete(id, userDetails.getId());
        return ResponseEntity.ok("Delete folder successfully!");
    }


    @GetMapping("{id}/topics")
    public ResponseEntity<PageDto<TopicDto>> getTopicsOfFolder(
            @PathVariable(value = "id") Long folderId,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long authId = userDetails != null ? userDetails.getId() : null;
        Pageable pageable = pageUtil.getPageable(pageIndex, pageSize, key, orderBy);
        PageDto<TopicDto> topics = topicService.searchTopicsInFolder(name, folderId, authId, pageable);
        return ResponseEntity.ok(topics);
    }

    @PostMapping("{id}/topics")
    public ResponseEntity<FolderDto> addTopicsToFolder(
            @PathVariable Long id,
            @RequestBody AddTopicRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FolderDto folderDto = folderService.addTopicsToFolder(id, request.getTopicIds(), userDetails.getId());
        return ResponseEntity.ok(folderDto);
    }

    @DeleteMapping("{id}/topics/{topicId}")
    public ResponseEntity<?> removeFolder(
            @PathVariable Long id,
            @PathVariable Long topicId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FolderDto dto = folderService.removeTopicFromFolder(id, topicId, userDetails.getId());
        return ResponseEntity.ok(dto);
    }
}
