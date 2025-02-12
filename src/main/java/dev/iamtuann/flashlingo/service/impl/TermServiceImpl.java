package dev.iamtuann.flashlingo.service.impl;

import dev.iamtuann.flashlingo.entity.Term;
import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.exception.BadRequestException;
import dev.iamtuann.flashlingo.exception.NoPermissionException;
import dev.iamtuann.flashlingo.exception.ResourceNotFoundException;
import dev.iamtuann.flashlingo.mapper.TermMapper;
import dev.iamtuann.flashlingo.model.TermDto;
import dev.iamtuann.flashlingo.model.request.TermRequest;
import dev.iamtuann.flashlingo.repository.TermRepository;
import dev.iamtuann.flashlingo.repository.TopicRepository;
import dev.iamtuann.flashlingo.service.TermService;
import dev.iamtuann.flashlingo.utils.CheckPermission;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class TermServiceImpl implements TermService {
    private final TermRepository termRepository;
    private final TopicRepository topicRepository;
    private final CheckPermission checkPermission;
    private final TermMapper termMapper = TermMapper.INSTANCE;

    @Override
    @Transactional
    public TermDto save(TermRequest request, Long userId) {
        if (!checkPermission.editableTopic(request.getTopicId(), userId)) {
            throw new NoPermissionException("edit this topic");
        }
        Term term;
        if (request.getId() == null) {
            term = createTerm(request);
        } else {
            term = termRepository.findByIdAndTopicId(request.getId(), request.getTopicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Term", "id", request.getId()));
            this.changeRank(request.getTopicId(), term.getRank(), request.getRank());
            termMapper.updateTermFormRequest(request, term);
        }
        Term newTerm = termRepository.save(term);
        return termMapper.toDto(newTerm);
    }

    @Override
    public List<TermDto> findAllByTopicId(Long topicId, Long userId) {
        if (!checkPermission.viewableTopic(topicId, userId)) {
            throw new NoPermissionException("view this topic");
        }
        List<Term> terms = termRepository.findAllByTopicIdOrderByRankAsc(topicId);
        return terms.stream().map(termMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(TermRequest request, Long userId) {
        if (!checkPermission.editableTopic(request.getTopicId(), userId)) {
            throw new NoPermissionException("edit this topic");
        }
        Term term = termRepository.findByIdAndTopicId(request.getId(), request.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("Term", "id", request.getId()));
        termRepository.delete(term);
        termRepository.decrementRanks(term.getTopic().getId(), term.getRank(), Integer.MAX_VALUE);
    }

    public void changeRank(Long topicId, Integer oldRank, Integer newRank) {
        if (newRank == null) {
            return;
        }
        if (newRank > oldRank) {
            termRepository.decrementRanks(topicId, oldRank, newRank);
        } else if (newRank < oldRank) {
            termRepository.incrementRanks(topicId, oldRank, newRank);
        }
    }

    public Term createTerm(TermRequest request) {
        Term term = new Term();
        Topic topic = topicRepository.findTopicById(request.getTopicId());
        if (request.getRank() == null) {
            throw new BadRequestException("rank", null, "New term must have ranking");
        } else if (request.getRank() > topic.getTerms().size()) {
            throw new BadRequestException("rank", request.getRank(), "Rank cannot be larger size");
        } else {
            termRepository.incrementRanks(request.getTopicId(), Integer.MAX_VALUE, request.getRank());
            term.setTopic(topic);
            termMapper.updateTermFormRequest(request, term);
        }
        return term;
    }
}
