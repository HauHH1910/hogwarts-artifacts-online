package com.hauhh.converter.user;

import com.hauhh.model.User;
import com.hauhh.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDTOConverter implements Converter<User, UserDTO> {

    @Override
    public UserDTO convert(User source) {
        return UserDTO.builder()
                .userID(source.getUserID())
                .username(source.getUsername())
                .enabled(source.isEnabled())
                .roles(source.getRoles())
                .build();
    }
}
