package dev.iamtuann.flashlingo.service;

import dev.iamtuann.flashlingo.model.FolderDto;
import dev.iamtuann.flashlingo.model.request.AddTopicRequest;
import dev.iamtuann.flashlingo.model.request.FolderRequest;

import java.util.Set;

public interface FolderService {

    Set<FolderDto> findAllFoldersCreated(long userId);

    Set<FolderDto> findAllPublicFoldersByUserId(long userId);

    FolderDto findFolderById(long id, Long userId);

    FolderDto create(FolderRequest request, long userId);

    FolderDto update(long id, FolderRequest request, long userId);

    void delete(long id, long userId);

    FolderDto addTopicsToFolder(AddTopicRequest request, long userId);
}
