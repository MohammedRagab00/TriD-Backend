package com.gotrid.trid.core.user.mapper;

import com.gotrid.trid.infrastructure.azure.ProfilePhotoService;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.api.user.dto.UserProfileResponse;
import com.gotrid.trid.api.admin.dto.UserSearchResponse;
import com.gotrid.trid.core.user.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class UserMapper {
    private final ProfilePhotoService profilePhotoService;

    public UserProfileResponse toProfileResponse(Users user) {
        return new UserProfileResponse(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getGender(),
                user.getDob(),
                profilePhotoService.getPhotoUrl(user.getPhoto())
        );
    }

    public UserSearchResponse toSearchResponse(Users user) {
        return new UserSearchResponse(
                user.getId(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );
    }
}
