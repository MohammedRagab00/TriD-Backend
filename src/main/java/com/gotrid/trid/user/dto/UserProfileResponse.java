package com.gotrid.trid.user.dto;

import com.gotrid.trid.user.domain.Gender;

public record UserProfileResponse(
        String firstName,
        String lastName,
        String email,
        Gender gender,
        int age,
        String photoUrl
) {
}
