package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.enums.EStatus;
import dev.iamtuann.flashlingo.model.AuthUserDto;
import dev.iamtuann.flashlingo.model.FolderDto;
import dev.iamtuann.flashlingo.model.PageDto;
import dev.iamtuann.flashlingo.model.TopicDto;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.FolderService;
import dev.iamtuann.flashlingo.service.TopicService;
import dev.iamtuann.flashlingo.service.UserService;
import dev.iamtuann.flashlingo.utils.PageUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final PageUtil pageUtil;
    private final TopicService topicService;
    private final FolderService folderService;

    @GetMapping("")
    public ResponseEntity<Page<AuthUserDto>> searchUsers(
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "orderBy", required = false) String orderBy
            ) {
        Integer status = EStatus.PUBLIC.getValue();
        Pageable pageable = pageUtil.getPageable(pageIndex, pageSize, key, orderBy);
        Page<AuthUserDto> users = userService.searchUsers(search, status, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthUserDto> getUser(@PathVariable(value = "id") Long userId) {
        AuthUserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/topics")
    public ResponseEntity<PageDto<TopicDto>> searchAuthUserTopics(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Pageable pageable = pageUtil.getPageable(pageIndex, pageSize, key, orderBy);
        PageDto<TopicDto> topics = topicService.searchTopics(name, null, userDetails.getId(), userDetails.getId(), pageable);
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/folders")
    public ResponseEntity<Page<FolderDto>> searchAuthUserFolders(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Pageable pageable = pageUtil.getPageable(pageIndex, pageSize, key, orderBy);
        Page<FolderDto> folders = folderService.searchFolders(name, userDetails.getId(), userDetails.getId(), pageable);
        return ResponseEntity.ok(folders);
    }

    @GetMapping("{id}/topics")
    public ResponseEntity<PageDto<TopicDto>> searchUserTopics(
            @PathVariable(value = "id") Long userId,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long authId = userDetails != null ? userDetails.getId() : null;
        Pageable pageable = pageUtil.getPageable(pageIndex, pageSize, key, orderBy);
        PageDto<TopicDto> topics = topicService.searchTopics(name, null, userId, authId, pageable);
        return ResponseEntity.ok(topics);
    }

    @GetMapping("{id}/folders")
    public ResponseEntity<Page<FolderDto>> searchUserFolders(
            @PathVariable(value = "id") Long userId,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long authId = userDetails != null ? userDetails.getId() : null;
        Pageable pageable = pageUtil.getPageable(pageIndex, pageSize, key, orderBy);
        Page<FolderDto> folders = folderService.searchFolders(name, userId, authId, pageable);
        return ResponseEntity.ok(folders);
    }
}
