package com.hauhh.service;

import com.hauhh.security.JwtProvider;
import com.hauhh.security.principal.MyUserPrincipal;
import com.hauhh.model.User;
import com.hauhh.converter.user.UserToUserDTOConverter;
import com.hauhh.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserToUserDTOConverter userToUserDTOConverter;

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        //Create user info
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();

        User user = principal.getUser();

        UserDTO userDTO = this.userToUserDTOConverter.convert(user);
        //Create a JWT
        String token = this.jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("userInfo", userDTO);

        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
