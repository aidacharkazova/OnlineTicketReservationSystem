package com.example.onlineticketreservationsystem.service.impl;

import com.example.onlineticketreservationsystem.dto.request.AppUserRequest;
import com.example.onlineticketreservationsystem.dto.response.AppUserResponse;
import com.example.onlineticketreservationsystem.mapper.UserMapper;
import com.example.onlineticketreservationsystem.model.entity.AppUser;
import com.example.onlineticketreservationsystem.repository.UserRepository;
import com.example.onlineticketreservationsystem.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private static final String CACHE_NAME = "users";
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public AppUserResponse registerUser(AppUserRequest request) {
        AppUser user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(List.of("USER"));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Cacheable(value = CACHE_NAME, key = "#id")
    @Override
    public AppUserResponse getUserById(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponse(user);
    }

    @Cacheable(value = CACHE_NAME, key = "'all'")
    @Override
    public List<AppUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }
}
