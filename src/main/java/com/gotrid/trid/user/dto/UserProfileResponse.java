package com.gotrid.trid.user.dto;

import com.gotrid.trid.user.domain.Gender;

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
