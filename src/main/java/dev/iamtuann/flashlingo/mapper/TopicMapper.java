package dev.iamtuann.flashlingo.mapper;

import com.github.slugify.Slugify;
import dev.iamtuann.flashlingo.entity.Term;
import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.model.TermDto;
import dev.iamtuann.flashlingo.model.TopicDto;
import dev.iamtuann.flashlingo.model.request.TopicRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicMapper {
    TopicMapper INSTANCE = Mappers.getMapper( TopicMapper.class );
    Slugify slg = Slugify.builder().build();

    TopicDto toDto(Topic topic);

    @Mapping(target = "topicId", source = "topic.id")
    TermDto termToTermDto(Term term);

    @Mapping(target = "terms", ignore = true)
    TopicDto toDtoWithoutTerms(Topic topic);

    default void updateTopicFromRequest(TopicRequest request, @MappingTarget Topic topic) {
        if ( request == null ) {
            return;
        }
        if ( request.getName() != null ) {
            topic.setName( request.getName() );
            topic.setSlug(slg.slugify(request.getName()));
        }
        if ( request.getDescription() != null ) {
            topic.setDescription( request.getDescription() );
        }
        if ( request.getTermLang() != null ) {
            topic.setTermLang( request.getTermLang() );
        }
        if ( request.getDefLang() != null ) {
            topic.setDefLang( request.getDefLang() );
        }
    }
}
