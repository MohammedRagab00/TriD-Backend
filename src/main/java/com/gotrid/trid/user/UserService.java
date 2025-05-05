package com.gotrid.trid.user;

import com.gotrid.trid.azure.ProfilePhotoService;
import com.gotrid.trid.exception.InvalidAgeException;
import com.gotrid.trid.exception.InvalidGenderException;
import com.gotrid.trid.user.dto.ChangePasswordRequest;
import com.gotrid.trid.user.dto.UpdateProfileRequest;
import com.gotrid.trid.user.dto.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
@Transactional
public class UserService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfilePhotoService profilePhotoService;

    public void changePassword(String email, ChangePasswordRequest request) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public void updateProfile(String email, UpdateProfileRequest request) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        try {
            user.setGender(Gender.valueOf(request.gender().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InvalidGenderException("Gender must be MALE, FEMALE, or PREFER_NOT_TO_SAY");
        }
        if (request.birthDate() != null) {
            validateAge(request.birthDate());
            user.setDob(request.birthDate());
        }

        userRepository.save(user);
    }

    private void validateAge(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now().minusYears(13))) {
            throw new InvalidAgeException("You must be at least 13 years old.");
        }
    }

    public void updatePhoto(String email, MultipartFile file) {
        profilePhotoService.updateProfilePhoto(email, file);
    }

    public void deletePhoto(String email) {
        profilePhotoService.deleteProfilePhoto(email);
    }

    public UserProfileResponse getUserProfile(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserProfileResponse(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getGender(),
                user.calculateAge(),
                profilePhotoService.getPhotoUrl(user.getPhoto())
        );
    }
}
