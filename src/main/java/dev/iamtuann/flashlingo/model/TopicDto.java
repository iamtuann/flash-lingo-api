package dev.iamtuann.flashlingo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    private Long id;
    private String name;
    private String description;
    private AuthUserDto createdBy;
    private Date createdAt;
    private Date updatedAt;
    private String slug;
    private String termLang;
    private String defLang;
    private Integer status;
    private Set<TermDto> terms;
}
