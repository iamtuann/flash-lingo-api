package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.model.AuthUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthUserMapper {
    AuthUserMapper INSTANCE = Mappers.getMapper( AuthUserMapper.class );

    @Mapping(target = "topicsNumber", expression = "java(authUser.getTopics() != null ? authUser.getTopics().size() : 0)")
    AuthUserDto toDto(AuthUser authUser);
}
