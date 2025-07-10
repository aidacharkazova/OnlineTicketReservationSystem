package com.example.onlineticketreservationsystem.mapper;


import com.example.onlineticketreservationsystem.dto.request.AppUserRequest;
import com.example.onlineticketreservationsystem.dto.response.AppUserResponse;
import com.example.onlineticketreservationsystem.model.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AppUserResponse toResponse(AppUser user);

    @Mapping(target = "tickets", ignore = true)
    AppUser toEntity(AppUserRequest user);

}
