package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.FolderDto;
import dev.iamtuann.flashlingo.model.PageDto;
import dev.iamtuann.flashlingo.model.request.AddTopicRequest;
import dev.iamtuann.flashlingo.model.request.FolderRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface FolderService {

    FolderDto findFolderById(long id, Long userId);

    PageDto<FolderDto> searchFolders(String name, Long userId, Long authId, Pageable pageable);

    FolderDto create(FolderRequest request, long userId);

    FolderDto update(long id, FolderRequest request, long userId);

    void delete(long id, long userId);

    FolderDto addTopicsToFolder(Long folderId, List<Long> topicIds, long userId);

    FolderDto removeTopicFromFolder(Long folderId, Long topicId, long userId);
}
