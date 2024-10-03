package com.hauhh.user;

import com.hauhh.model.User;
import com.hauhh.repository.UserRepository;
import com.hauhh.service.UserService;
import com.hauhh.exception.ObjectNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private User user;

    private User user1;

    private List<User> users;

    @BeforeEach
    void setUp() {

        this.users = new ArrayList<>();

        user = User.builder()
                .userID(1)
                .username("Game")
                .password("pw")
                .roles("user")
                .enabled(true)
                .build();


        user1 = User.builder()
                .userID(2)
                .username("Ngoc")
                .password("pw")
                .roles("manager")
                .enabled(true)
                .build();

        this.users.add(user);
        this.users.add(user1);
    }

    @Test
    void testCreateUserSuccess() {
        //Given
        given(this.passwordEncoder.encode(user.getPassword())).willReturn("pw");
        given(userRepository.save(user)).willReturn(user);
        //When
        User createdUser = userService.createUser(user);
        //Then
        assertThat(createdUser.getUsername()).isEqualTo("Game");
        assertThat(createdUser.getPassword()).isEqualTo("pw");
        assertThat(createdUser.getRoles()).isEqualTo("user");
        assertThat(createdUser.isEnabled()).isTrue();

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetAllUserSuccess() {
        //Given
        given(userRepository.findAll()).willReturn(this.users);
        //When
        List<User> allUsers = userService.getAllUsers();
        //Then
        Assertions.assertThat(allUsers).isNotNull();
        assertThat(allUsers.size()).isEqualTo(this.users.size());
        assertThat(allUsers.get(0).getUsername()).isEqualTo("Game");
        assertThat(allUsers.get(0).getPassword()).isEqualTo("pw");
        assertThat(allUsers.get(0).getRoles()).isEqualTo("user");
        assertThat(allUsers.get(1).getUsername()).isEqualTo("Ngoc");
        assertThat(allUsers.get(1).getPassword()).isEqualTo("pw");
        assertThat(allUsers.get(1).getRoles()).isEqualTo("manager");

        verify(userRepository, times(1)).findAll();

    }

    @Test
    void testGetUserByIDSuccess() {
        //Given
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        //When
        User getUser = userService.getUserById(1);
        //Then
        assertThat(getUser.getUserID()).isEqualTo(1);
        assertThat(getUser.getUsername()).isEqualTo("Game");
        assertThat(getUser.getPassword()).isEqualTo("pw");
        assertThat(getUser.getRoles()).isEqualTo("user");

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUserByIDNotFound() {
        //Given
        given(userRepository.findById(1)).willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> {
            userService.getUserById(1);
        });
        //Then
        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testUpdateUserSuccess() {
        //given
        User existingUser = User.builder()
                .username("Game")
                .enabled(true)
                .roles("user")
                .build();

        User updatedUser = User.builder()
                .username("John")
                .enabled(true)
                .roles("admin user")
                .build();

        given(userRepository.findById(1)).willReturn(Optional.of(existingUser));

        given(userRepository.save(any(User.class))).willReturn(updatedUser);
        //when
        User update = userService.updateUser(1, existingUser);
        //then

        assertThat(update.getUsername()).isEqualTo("John");
        assertThat(update.getRoles()).isEqualTo("admin user");
        assertThat(update.isEnabled()).isTrue();

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateNotFound(){
        //Given
        given(userRepository.findById(1)).willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> {
            userService.updateUser(1, user);
        });
        //Then
        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void testDeleteUserSuccess() {
        //Given
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1);
        //When
        userService.deleteUser(1);
        //Then
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUserNotFound(){
        //Given
        given(userRepository.findById(1)).willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> {
            userService.deleteUser(1);
        });
        //Then
        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
    }
}