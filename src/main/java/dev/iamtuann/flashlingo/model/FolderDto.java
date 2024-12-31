package dev.iamtuann.flashlingo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class FolderDto {
    private Long id;
    private String name;
    private AuthUserDto createdBy;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private String slug;
    private Integer itemsNumber;
    private Set<Long> topicIds;
}
