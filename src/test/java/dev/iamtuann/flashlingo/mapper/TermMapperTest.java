package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.Term;
import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.model.TermDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TermMapperTest {
    private final TermMapper termMapper = TermMapper.INSTANCE;

    protected static Term mockTerm() {
        Topic topic = new Topic();
        topic.setId(1L);
        Term term = new Term();
        term.setId(1L);
        term.setTerm("Hello");
        term.setDefinition("Xin chao");
        term.setRank(1);
        term.setImageUrl("");
        term.setTopic(topic);
        return term;
    }

    @Test
    public void testTermToTermDto() {
        Term term = mockTerm();
        TermDto termDto = termMapper.toDto(term);

        assertNotNull(termDto);
        assertEquals(term.getId(), termDto.getId());
        assertEquals(term.getTerm(), termDto.getTerm());
        assertEquals(term.getDefinition(), termDto.getDefinition());
        assertEquals(term.getTopic().getId(), termDto.getTopicId());
    }
}
