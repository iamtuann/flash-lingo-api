package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.TermDto;
import dev.iamtuann.flashlingo.model.request.TermRequest;

public interface TermService {
    TermDto save(TermRequest request, Long userId);
    void delete(TermRequest request, Long userId);
}
