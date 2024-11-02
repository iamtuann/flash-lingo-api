package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.model.TopicDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicMapper {
    TopicMapper INSTANCE = Mappers.getMapper( TopicMapper.class );

    TopicDto toDto(Topic topic);

    @Mapping(target = "cards", ignore = true)
    TopicDto toDtoWithoutCards(Topic topic);
}
