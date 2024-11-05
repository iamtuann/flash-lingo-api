package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.Card;
import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.model.TopicDto;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TopicMapperTest {
    private final TopicMapper topicMapper = TopicMapper.INSTANCE;

    protected static Topic mockTopic() {
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Test");
        topic.setSlug("test");
        topic.setCreatedBy(AuthUserMapperTest.mockAuthUser());
        Set<Card> cards = new HashSet<>();
        cards.add(CardMapperTest.mockCard());
        topic.setCards(cards);
        return topic;
    }

    @Test
    public void testTopicToTopicDto() {
        Topic topic = mockTopic();
        TopicDto topicDto = topicMapper.toDto(topic);

        assertNotNull(topicDto);
        assertEquals(topic.getId(), topicDto.getId());
        assertEquals(topic.getName(), topicDto.getName());
        assertEquals(topic.getCreatedBy().getId(), topicDto.getCreatedBy().getId());
        assertNotNull(topicDto.getCards());
    }

    @Test
    public void testTopicToTopicDtoWithoutCard() {
        Topic topic = mockTopic();
        TopicDto topicDto = topicMapper.toDtoWithoutCards(topic);

        assertNotNull(topicDto);
        assertEquals(topic.getId(), topicDto.getId());
        assertEquals(topic.getName(), topicDto.getName());
        assertEquals(topic.getCreatedBy().getId(), topicDto.getCreatedBy().getId());
        assertNull(topicDto.getCards());
    }
}
