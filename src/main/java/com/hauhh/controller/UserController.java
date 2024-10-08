package com.hauhh.controller;

import com.hauhh.common.Result;
import com.hauhh.common.StatusCode;
import com.hauhh.service.UserService;
import com.hauhh.converter.user.UserDTOToUserConverter;
import com.hauhh.converter.user.UserToUserDTOConverter;
import com.hauhh.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.url}/users")
public class UserController {

    private final UserService userService;
    private final UserToUserDTOConverter userToUserDTOConverter;
    private final UserDTOToUserConverter userDTOToEntityConverter;

    @GetMapping
    public Result<List<UserDTO>> getAllUsers() {
        return Result.<List<UserDTO>>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Find All Success")
                .data(this.userService.getAllUsers().stream()
                        .map(this.userToUserDTOConverter::convert).toList())
                .build();
    }

    @GetMapping("/{userID}")
    public Result<UserDTO> getUserByID(@PathVariable Integer userID) {
        return Result.<UserDTO>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Find One Success")
                .data(this.userToUserDTOConverter.convert(
                        this.userService.getUserById(userID)
                ))
                .build();
    }

    @PostMapping
    public Result<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return Result.<UserDTO>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Add Success")
                .data(this.userToUserDTOConverter.convert(
                        this.userService.createUser(
                                Objects.requireNonNull(this.userDTOToEntityConverter.convert(
                                        userDTO
                                ))
                        )
                ))
                .build();
    }

    @PutMapping("/{userID}")
    public Result<UserDTO> updateUser(@PathVariable Integer userID, @RequestBody UserDTO userDTO) {
        return Result.<UserDTO>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Update Success")
                .data(this.userToUserDTOConverter.convert(
                        this.userService.updateUser(userID,
                                this.userDTOToEntityConverter.convert(
                                        userDTO
                                )
                        )
                ))

                .build();
    }

    @DeleteMapping("/{userID}")
    public Result<Void> deleteUser(@PathVariable Integer userID) {
        this.userService.deleteUser(userID);
        return Result.<Void>builder()
                .flag(true)
                .code(StatusCode.SUCCESS)
                .message("Delete Success")
                .data(null)
                .build();
    }


}
