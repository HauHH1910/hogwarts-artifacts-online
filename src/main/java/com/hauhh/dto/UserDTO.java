package com.hauhh.dto;

import lombok.Builder;

@Builder
public record UserDTO(
        Integer userID,
        String username,
        boolean enabled,
        String roles
) {
}
