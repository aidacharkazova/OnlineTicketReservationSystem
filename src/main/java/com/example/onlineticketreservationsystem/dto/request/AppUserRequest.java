package com.example.onlineticketreservationsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AppUserRequest {

    @NotBlank(message = "Fullname cannot be empty!")
    private String fullName;

    @Email(message = "Invalid email format. Example: name@example.com")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "Password must be at least 8 characters and include uppercase, lowercase, and a number")
    private String password;

}
