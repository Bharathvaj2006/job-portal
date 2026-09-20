package com.jobportal.service;

import com.jobportal.dto.CreateUserRequest;
import com.jobportal.entity.User;
import com.jobportal.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserRequest request) {

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );

        return userRepository.save(user);
    }
}