package com.gotrid.trid.api.user.dto;

import com.gotrid.trid.core.user.model.Gender;

import java.time.LocalDate;

public record UserProfileResponse(
        String firstName,
        String lastName,
        String email,
        Gender gender,
        int age,
        LocalDate birthDate,
        String photoUrl

) {
}
