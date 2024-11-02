package dev.iamtuann.flashlingo.mapper;

import dev.iamtuann.flashlingo.entity.AuthUser;
import dev.iamtuann.flashlingo.entity.Role;
import dev.iamtuann.flashlingo.model.AuthUserDto;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class AuthUserMapperTest {
    private final AuthUserMapper authUserMapper = AuthUserMapper.INSTANCE;

    protected static AuthUser mockAuthUser() {
        Role role = new Role(3L, "USER", "User");
        AuthUser user = new AuthUser();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setFirstName("first_name");
        user.setLastName("last_name");
        user.setPassword("123456");
        user.setProvider("System");
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        return user;
    }

    @Test
    public void testAuthUserToAuthUserDto() {
        AuthUser user = mockAuthUser();

        AuthUserDto userDto = authUserMapper.toDto(user);
        assertNotNull(userDto);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
    }
}
