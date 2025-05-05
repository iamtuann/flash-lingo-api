package dev.iamtuann.flashlingo.mapper;

import com.github.slugify.Slugify;
import dev.iamtuann.flashlingo.entity.Folder;
import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.model.FolderDto;
import dev.iamtuann.flashlingo.model.request.FolderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AuthUserMapper.class})
public interface FolderMapper {
    FolderMapper INSTANCE = Mappers.getMapper( FolderMapper.class );
    Slugify slg = Slugify.builder().build();

    @Mapping(target = "itemsNumber", expression = "java(folder.getTopics().size())")
    @Mapping(source = "topics", target = "topicIds", qualifiedByName = "topicToId")
    FolderDto toDto(Folder folder);

    @Named("topicToId")
    static Long topicToId(Topic topic) {
        return topic.getId();
    }

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
