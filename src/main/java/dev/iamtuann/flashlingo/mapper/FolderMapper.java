package dev.iamtuann.flashlingo.mapper;

import com.github.slugify.Slugify;
import dev.iamtuann.flashlingo.entity.Folder;
import dev.iamtuann.flashlingo.model.FolderDto;
import dev.iamtuann.flashlingo.model.request.FolderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FolderMapper {
    FolderMapper INSTANCE = Mappers.getMapper( FolderMapper.class );
    Slugify slg = Slugify.builder().build();

    FolderDto toDto(Folder folder);

    @Mapping(target = "topics", ignore = true)
    FolderDto toDtoWithoutTopics(Folder folder);

    default void updateFolderFromRequest(FolderRequest request, @MappingTarget Folder folder) {
        if ( request == null ) {
            return;
        }
        if ( request.getName() != null ) {
            folder.setName( request.getName() );
            folder.setSlug(slg.slugify(request.getName()));
        }
        if ( request.getDescription() != null ) {
            folder.setDescription( request.getDescription() );
        }
        if ( request.getStatus() != null ) {
            folder.setStatus( request.getStatus() );
        }
    }
}
