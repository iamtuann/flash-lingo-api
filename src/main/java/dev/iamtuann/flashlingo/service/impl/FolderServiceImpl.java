package dev.iamtuann.flashlingo.service.impl;

import dev.iamtuann.flashlingo.entity.Folder;
import dev.iamtuann.flashlingo.enums.EStatus;
import dev.iamtuann.flashlingo.exception.NoPermissionException;
import dev.iamtuann.flashlingo.mapper.FolderMapper;
import dev.iamtuann.flashlingo.model.FolderDto;
import dev.iamtuann.flashlingo.model.PageDto;
import dev.iamtuann.flashlingo.model.request.AddTopicRequest;
import dev.iamtuann.flashlingo.model.request.FolderRequest;
import dev.iamtuann.flashlingo.repository.AuthUserRepository;
import dev.iamtuann.flashlingo.repository.FolderRepository;
import dev.iamtuann.flashlingo.repository.TopicRepository;
import dev.iamtuann.flashlingo.service.FolderService;
import dev.iamtuann.flashlingo.utils.CheckPermission;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private final CheckPermission checkPermission;
    private final FolderMapper folderMapper = FolderMapper.INSTANCE;
    private final AuthUserRepository authUserRepository;
    private final TopicRepository topicRepository;

    @Override
    public FolderDto findFolderById(long id, Long userId) {
        if (checkPermission.viewableFolder(id, userId)) {
            Folder folder = folderRepository.findFolderById(id);
            return folderMapper.toDto(folder);
        } else {
            throw new NoPermissionException("access this resource");
        }
    }

    @Override
    public PageDto<FolderDto> searchFolders(String name, Long userId, Long authId, Pageable pageable) {
        Integer status = EStatus.PUBLIC.getValue();
        if (authId != null && Objects.equals(userId, authId)) {
            status = null;
        }
        Page<Folder> folderPage = folderRepository.searchFolders(name, status, userId, pageable);
        List<FolderDto> folders = folderPage.stream()
                .map(folderMapper::toDto).toList();
        return new PageDto<>(folders, folderPage);
    }

    @Override
    public FolderDto create(FolderRequest request, long userId) {
        Folder folder = new Folder();
        folder.setStatus(EStatus.PUBLIC.getValue());
        folder.setCreatedBy(authUserRepository.findAuthUserById(userId));
        folder.setTopics(new HashSet<>());
        folderMapper.updateFolderFromRequest(request, folder);
        folder = folderRepository.save(folder);
        return folderMapper.toDto(folder);
    }

    @Override
    public FolderDto update(long id, FolderRequest request, long userId) {
        if (!checkPermission.editableFolder(id, userId)) {
            throw new NoPermissionException("edit this folder");
        }
        Folder folder = folderRepository.findFolderById(id);
        folderMapper.updateFolderFromRequest(request, folder);
        folder = folderRepository.save(folder);
        return folderMapper.toDto(folder);
    }

    @Override
    public void delete(long id, long userId) {
        if (checkPermission.editableFolder(id, userId)) {
            folderRepository.deleteById(id);
        } else {
            throw new NoPermissionException("delete this folder");
        }
    }

    @Override
    public FolderDto addTopicsToFolder(AddTopicRequest request, long userId) {
        if (!checkPermission.editableFolder(request.getFolderId(), userId)) {
            throw new NoPermissionException("edit this folder");
        }
        Folder folder = folderRepository.findFolderById(request.getFolderId());
        for(Long id : request.getTopicIds()) {
            if(checkPermission.viewableTopic(id, userId)) {
                folder.getTopics().add(topicRepository.findTopicById(id));
            } else {
                throw new NoPermissionException("access this topic");
            }
        }
        folder.setUpdatedAt(new Date());
        folderRepository.save(folder);
        return folderMapper.toDto(folder);
    }
}
