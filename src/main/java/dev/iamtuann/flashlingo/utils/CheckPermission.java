package dev.iamtuann.flashlingo.utils;

import dev.iamtuann.flashlingo.entity.Folder;
import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.enums.EStatus;
import dev.iamtuann.flashlingo.exception.ResourceNotFoundException;
import dev.iamtuann.flashlingo.repository.FolderRepository;
import dev.iamtuann.flashlingo.repository.TopicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CheckPermission {
    private final TopicRepository topicRepository;
    private final FolderRepository folderRepository;

    public boolean editableTopic(Long topicId, Long userId) {
        if (topicId == null) return true;
        if (userId == null) return false;
        if (!topicRepository.existsById(topicId)) {
            throw new ResourceNotFoundException("Topic", "id", topicId);
        }
        return topicRepository.existsByIdAndCreatedById(topicId, userId);
    }

    public boolean viewableTopic(Long topicId, Long userId) {
        if (topicId == null) throw new IllegalArgumentException("topicId is not null");
        if (!topicRepository.existsById(topicId)) {
            throw new ResourceNotFoundException("Topic", "id", topicId);
        }
        Topic topic = topicRepository.findTopicById(topicId);
        return topic.getStatus().equals(EStatus.PUBLIC.getValue()) || topic.getCreatedBy().getId().equals(userId);
    }

    public boolean viewableFolder(Long folderId, Long userId) {
        if (!folderRepository.existsById(folderId)) {
            throw new ResourceNotFoundException("Folder", "id", folderId);
        }
        Folder folder = folderRepository.findFolderById(folderId);
        return folder.getStatus().equals(EStatus.PUBLIC.getValue()) || folder.getCreatedBy().getId().equals(userId);
    }

    public boolean editableFolder(Long folderId, Long userId) {
        if (folderId == null)  throw new IllegalArgumentException("folderId is not null");
        if (userId == null) return false;
        if (!folderRepository.existsById(folderId)) {
            throw new ResourceNotFoundException("Folder", "id", folderId);
        }
        return folderRepository.existsByIdAndCreatedById(folderId, userId);
    }
}
