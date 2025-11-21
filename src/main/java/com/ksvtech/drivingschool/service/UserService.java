package com.ksvtech.drivingschool.service;

import com.ksvtech.drivingschool.entity.Role;
import com.ksvtech.drivingschool.entity.User;
import com.ksvtech.drivingschool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createStudentUser(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already used");
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .roles(Set.of(Role.STUDENT))
                .enabled(true)
                .build();
        return userRepository.save(user);
    }
}
