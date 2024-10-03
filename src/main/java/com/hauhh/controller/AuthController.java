package com.hauhh.controller;

import com.hauhh.service.AuthService;
import com.hauhh.common.Result;
import com.hauhh.common.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(("${api.endpoint.url}/auth"))
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result getLoginInfo(Authentication authentication){
        log.info("getLoginInfo");
        log.debug("authentication user: {}", authentication.getName());
        return Result.builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("User INFO AND JWT Web token")
                .data(this.authService.createLoginInfo(authentication))
                .build();
    }
}
