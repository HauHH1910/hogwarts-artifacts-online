package com.hauhh.service;

import com.hauhh.model.User;
import com.hauhh.exception.ObjectNotFoundException;
import com.hauhh.security.principal.MyUserPrincipal;
import com.hauhh.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user with user name: " + username));
    }

    public User createUser(User request) {
        this.passwordEncoder.encode(request.getPassword());
        return this.userRepository.save(request);
    }

    public User getUserById(Integer userID) {
        return this.userRepository.findById(userID).orElseThrow(() -> new ObjectNotFoundException("user", userID));
    }

    public User updateUser(Integer userID, User exsitingUser) {
        return this.userRepository.findById(userID).map(user -> {

            user.setUsername(exsitingUser.getUsername());
            user.setEnabled(exsitingUser.isEnabled());
            user.setRoles(exsitingUser.getRoles());

            return this.userRepository.save(user);
        }).orElseThrow(() -> new ObjectNotFoundException("user", userID));
    }

    public void deleteUser(Integer userID) {
        this.userRepository.findById(userID).orElseThrow(() -> new ObjectNotFoundException("user", userID));
        this.userRepository.deleteById(userID);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
