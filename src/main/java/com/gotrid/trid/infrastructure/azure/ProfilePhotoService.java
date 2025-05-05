package com.gotrid.trid.infrastructure.azure;

import com.gotrid.trid.user.domain.Users;
import com.gotrid.trid.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
@Slf4j
public class ProfilePhotoService {
    private final AzureStorageService azureStorageService;
    private final UsersRepository userRepository;

    @Value("${azure.storage.pp-container-name}")
    private String CONTAINER_NAME;

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_TYPES = List.of(
            "image/jpeg", "image/png", "image/jpg"
    );

    public void updateProfilePhoto(String email, MultipartFile file) {
        var user = getUserByEmail(email);

        String filename = UUID.randomUUID() + getFileExtension(file);
        String blobName = azureStorageService.uploadFile(file, CONTAINER_NAME, filename,
                MAX_SIZE, ALLOWED_TYPES);

        if (user.getPhoto() != null) {
            azureStorageService.deleteFile(user.getPhoto(), CONTAINER_NAME);
        }
        user.setPhoto(blobName);
        userRepository.save(user);
    }

    public void deleteProfilePhoto(String email) {
        var user = getUserByEmail(email);

        if (user.getPhoto() != null) {
            azureStorageService.deleteFile(user.getPhoto(), CONTAINER_NAME);
            user.setPhoto(null);
            userRepository.save(user);
        }
    }

    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
    }

    public String getPhotoUrl(String photoName) {
        if (photoName == null) {
            return null;
        }
        return azureStorageService.getBlobUrlWithSas(CONTAINER_NAME, photoName);
    }

    private Users getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}