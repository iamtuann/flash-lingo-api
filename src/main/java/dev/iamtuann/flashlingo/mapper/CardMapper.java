package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.Card;
import dev.iamtuann.flashlingo.model.CardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CardMapper {
    CardMapper INSTANCE = Mappers.getMapper( CardMapper.class );

    @Mapping(target = "topicId", source = "topic.id")
    CardDto toDto(Card card);
}
