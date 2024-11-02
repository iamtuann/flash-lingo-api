package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.Card;
import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.model.CardDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CardMapperTest {
    private final CardMapper cardMapper = CardMapper.INSTANCE;

    protected static Card mockCard() {
        Topic topic = new Topic();
        topic.setId(1L);
        Card card = new Card();
        card.setId(1L);
        card.setWord("Hello");
        card.setDefinition("Xin chao");
        card.setRank(1);
        card.setImageUrl("");
        card.setTopic(topic);
        return card;
    }

    @Test
    public void testCardToCardDto() {
        Card card = mockCard();
        CardDto cardDto = cardMapper.toDto(card);

        assertNotNull(cardDto);
        assertEquals(card.getId(), cardDto.getId());
        assertEquals(card.getWord(), cardDto.getWord());
        assertEquals(card.getDefinition(), cardDto.getDefinition());
        assertEquals(card.getTopic().getId(), cardDto.getTopicId());
    }
}
