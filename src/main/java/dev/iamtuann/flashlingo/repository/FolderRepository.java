package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository(value = "folderRepository")
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findFolderById(long id);

    Set<Folder> findAllByCreatedById(long userId);

    Set<Folder> findAllByCreatedByIdAndStatus(long userId, int status);

    boolean existsByIdAndCreatedById(long id, long userId);
}
