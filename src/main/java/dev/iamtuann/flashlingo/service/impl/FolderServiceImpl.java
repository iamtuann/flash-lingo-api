package dev.iamtuann.flashlingo.service.impl;

import dev.iamtuann.flashlingo.entity.Folder;
import dev.iamtuann.flashlingo.enums.EStatusMode;
import dev.iamtuann.flashlingo.exception.NoPermissionException;
import dev.iamtuann.flashlingo.mapper.FolderMapper;
import dev.iamtuann.flashlingo.model.FolderDto;
import dev.iamtuann.flashlingo.model.request.FolderRequest;
import dev.iamtuann.flashlingo.repository.FolderRepository;
import dev.iamtuann.flashlingo.service.FolderService;
import dev.iamtuann.flashlingo.utils.CheckPermission;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private final CheckPermission checkPermission;
    private final FolderMapper folderMapper = FolderMapper.INSTANCE;

    @Override
    public Set<FolderDto> findAllFoldersCreated(long userId) {
        Set<Folder> folders = folderRepository.findAllByCreatedById(userId);
        return folders.stream().map(folderMapper::toDto).collect(Collectors.toSet());
    }

    @Override
    public Set<FolderDto> findAllPublicFoldersByUserId(long userId) {
        Set<Folder> folders = folderRepository.findAllByCreatedByIdAndStatus(userId, EStatusMode.PUBLIC.getValue());
        return folders.stream().map(folderMapper::toDto).collect(Collectors.toSet());
    }

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
    public FolderDto create(FolderRequest request, long userId) {
        Folder folder = new Folder();
        folder.setStatus(EStatusMode.PUBLIC.getValue());
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
}
