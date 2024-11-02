package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.Folder;
import dev.iamtuann.flashlingo.entity.Topic;
import dev.iamtuann.flashlingo.model.FolderDto;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FolderMapperTest {
    private final FolderMapper folderMapper = FolderMapper.INSTANCE;

    protected static Folder mockFolder() {
        Folder folder = new Folder();
        folder.setId(1L);
        folder.setName("Folder Test");
        folder.setSlug("folder-test");
        Set<Topic> topics = new HashSet<>();
        topics.add(TopicMapperTest.mockTopic());
        folder.setTopics(topics);
        folder.setAuthUser(AuthUserMapperTest.mockAuthUser());
        return folder;
    }

    @Test
    public void testFolderToFolderDto() {
        Folder folder = mockFolder();
        FolderDto folderDto = folderMapper.toDto(folder);
        assertNotNull(folderDto);
        assertEquals(folder.getId(), folderDto.getId());
        assertEquals(folder.getName(), folderDto.getName());
        assertEquals(folder.getSlug(), folderDto.getSlug());
        assertNotNull(folderDto.getTopics());
        assertEquals(1, folderDto.getTopics().size());
    }

    @Test
    public void testFolderToFolderDtoWithoutTopics() {
        Folder folder = mockFolder();
        FolderDto folderDto = folderMapper.toDtoWithoutTopics(folder);
        assertNotNull(folderDto);
        assertEquals(folder.getId(), folderDto.getId());
        assertEquals(folder.getName(), folderDto.getName());
        assertEquals(folder.getSlug(), folderDto.getSlug());
        assertNull(folderDto.getTopics());
    }
}
