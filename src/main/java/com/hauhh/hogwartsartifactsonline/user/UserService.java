package com.hauhh.hogwartsartifactsonline.user;

import com.hauhh.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        return this.userRepository.save(user);
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
