package com.gotrid.trid.user.dto;

import com.gotrid.trid.user.Gender;

public record UserProfileResponse(
        String firstName,
        String lastName,
        String email,
        Gender gender,
        int age,
        String photoUrl
) {
}
