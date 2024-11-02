package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.Folder;
import dev.iamtuann.flashlingo.model.FolderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FolderMapper {
    FolderMapper INSTANCE = Mappers.getMapper( FolderMapper.class );

    FolderDto toDto(Folder folder);

    @Mapping(target = "topics", ignore = true)
    FolderDto toDtoWithoutTopics(Folder folder);
}
