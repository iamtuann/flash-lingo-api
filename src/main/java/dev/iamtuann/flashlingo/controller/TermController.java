package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.TermDto;
import dev.iamtuann.flashlingo.model.request.TermRequest;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.TermService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/terms")
public class TermController {
    private final TermService termService;

    @PostMapping("")
    public ResponseEntity<TermDto> saveTerm(@RequestBody TermRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TermDto termDto = termService.save(request, userDetails != null ? userDetails.getId() : null);
        return ResponseEntity.ok(termDto);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteTerm(@RequestBody TermRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        termService.delete(request, userDetails.getId());
        return ResponseEntity.ok("Deleted term successfully!");
    }
}
