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
            term.setTerm(request.getTerm().trim());
        }
        if ( request.getDefinition() != null ) {
            term.setDefinition(request.getDefinition().trim());
        }
        if ( request.getPronunciation() != null ) {
            term.setPronunciation(request.getPronunciation().trim());
        }
        if ( request.getLevel() != null ) {
            term.setLevel(request.getLevel());
        }
        if ( request.getExample() != null ) {
            term.setExample(request.getExample().trim());
        }
        if ( request.getPartOfSpeech() != null ) {
            term.setPartOfSpeech(request.getPartOfSpeech());
        }
        if ( request.getRank() != null ) {
            term.setRank(request.getRank());
        }

    }
}
