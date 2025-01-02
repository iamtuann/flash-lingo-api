package dev.iamtuann.flashlingo.repository;

import dev.iamtuann.flashlingo.entity.Folder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository(value = "folderRepository")
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findFolderById(long id);

    @Query(value = "SELECT f FROM Folder f " +
            "WHERE (:name IS NULL OR :name = '' OR (f.name LIKE CONCAT('%', :name, '%'))) " +
            "AND (:status IS NULL OR f.status = :status) " +
            "AND (:userId IS NULL OR f.createdBy.id = :userId)" )
    Page<Folder> searchFolders(@Param("name") String name,
                             @Param("status") Integer status,
                             @Param("userId") Long userId,
                             Pageable pageable);

    boolean existsByIdAndCreatedById(long id, long userId);
}
