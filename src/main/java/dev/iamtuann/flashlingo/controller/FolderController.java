package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.FolderDto;
import dev.iamtuann.flashlingo.model.request.AddTopicRequest;
import dev.iamtuann.flashlingo.model.request.FolderRequest;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.FolderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/folders")
@AllArgsConstructor
public class FolderController {
    private final FolderService folderService;

    @GetMapping("{id}")
    public ResponseEntity<FolderDto> getFolder(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FolderDto folderDto = folderService.findFolderById(id, userDetails != null ? userDetails.getId() : null);
        return ResponseEntity.ok(folderDto);
    }

    @PostMapping("")
    public ResponseEntity<FolderDto> createFolder(@RequestBody FolderRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
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

    @PostMapping("/topics")
    public ResponseEntity<FolderDto> addTopicToFolder(@RequestBody AddTopicRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FolderDto folderDto = folderService.addTopicsToFolder(request, userDetails.getId());
        return ResponseEntity.ok(folderDto);
    }
}
