package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.TermDto;
import dev.iamtuann.flashlingo.model.request.TermRequest;

import java.util.List;

public interface TermService {
    TermDto save(TermRequest request, Long userId);
    List<TermDto> findAllByTopicId(Long topicId, Long userId);
    void delete(TermRequest request, Long userId);
}
