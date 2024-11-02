package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.model.AuthUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthUserMapper {
    AuthUserMapper INSTANCE = Mappers.getMapper( AuthUserMapper.class );

    AuthUserDto toDto(AuthUser authUser);
}
