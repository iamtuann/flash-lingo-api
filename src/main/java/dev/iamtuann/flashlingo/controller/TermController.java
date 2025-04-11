package dev.iamtuann.flashlingo.controller;

import dev.iamtuann.flashlingo.model.TermDto;
import dev.iamtuann.flashlingo.model.request.ListTermRequest;
import dev.iamtuann.flashlingo.model.request.TermRequest;
import dev.iamtuann.flashlingo.security.UserDetailsImpl;
import dev.iamtuann.flashlingo.service.TermService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/terms")
public class TermController {
    private final TermService termService;

    @GetMapping("")
    public ResponseEntity<List<TermDto>> getTermsTopic(@RequestParam(value = "topicId") long topicId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<TermDto> terms = termService.findAllByTopicId(topicId, userDetails != null ? userDetails.getId() : null);
        return ResponseEntity.ok(terms);
    }

    @PostMapping("")
    public ResponseEntity<TermDto> saveTerm(@Valid @RequestBody TermRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TermDto termDto = termService.save(request, userDetails != null ? userDetails.getId() : null);
        return ResponseEntity.ok(termDto);
    }

    @PostMapping("list")
    public ResponseEntity<List<TermDto>> saveListTerm(@Valid @RequestBody ListTermRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<TermDto> response = termService.saveList(request, userDetails != null ? userDetails.getId() : null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteTerm(@RequestBody TermRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        termService.delete(request, userDetails.getId());
        return ResponseEntity.ok("Deleted term successfully!");
    }
}
