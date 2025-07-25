package com.example.onlineticketreservationsystem.service.interfaces;

import com.example.onlineticketreservationsystem.dto.request.AppUserRequest;
import com.example.onlineticketreservationsystem.dto.response.AppUserResponse;

import java.util.List;

public interface UserService {
//    AppUserResponse registerUser(AppUserRequest request);

    AppUserResponse getUserById(Long id);

    List<AppUserResponse> getAllUsers();
}
