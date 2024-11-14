package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.Term;
import dev.iamtuann.flashlingo.model.TermDto;
import dev.iamtuann.flashlingo.model.request.TermRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TermMapper {
    TermMapper INSTANCE = Mappers.getMapper( TermMapper.class );

    @Mapping(target = "topicId", source = "topic.id")
    TermDto toDto(Term term);

    default void updateTermFormRequest(TermRequest request,@MappingTarget Term term) {
        if ( request == null ) {
            return;
        }
        if ( request.getTerm() != null ) {
            term.setTerm(request.getTerm());
        }
        if ( request.getDefinition() != null ) {
            term.setDefinition(request.getDefinition());
        }
        if ( request.getRank() != null ) {
            term.setRank(request.getRank());
        }

    }
}
