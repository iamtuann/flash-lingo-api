package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.Term;
import dev.iamtuann.flashlingo.model.TermDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TermMapper {
    TermMapper INSTANCE = Mappers.getMapper( TermMapper.class );

    @Mapping(target = "topicId", source = "topic.id")
    TermDto toDto(Term term);
}
