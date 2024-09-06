package com.hauhh.hogwartsartifactsonline.user.converter;

import com.hauhh.hogwartsartifactsonline.user.User;
import com.hauhh.hogwartsartifactsonline.user.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDTOToUserConverter implements Converter<UserDTO, User> {

    @Override
    public User convert(UserDTO source) {
        return User.builder()
                .username(source.username())
                .enabled(source.enabled())
                .roles(source.roles())
                .build();
    }
}
