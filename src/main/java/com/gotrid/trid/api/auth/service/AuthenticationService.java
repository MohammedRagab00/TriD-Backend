package com.gotrid.trid.api.auth.service;

import com.gotrid.trid.api.auth.dto.AuthenticationRequest;
import com.gotrid.trid.api.auth.dto.AuthenticationResponse;
import com.gotrid.trid.api.auth.dto.RegistrationRequest;
import com.gotrid.trid.common.exception.custom.EmailAlreadyExistsException;
import com.gotrid.trid.common.exception.custom.InvalidRefreshTokenException;
import com.gotrid.trid.common.exception.custom.InvalidTokenException;
import com.gotrid.trid.common.exception.custom.TokenExpiredException;
import com.gotrid.trid.common.exception.custom.user.InvalidAgeException;
import com.gotrid.trid.common.exception.custom.user.InvalidGenderException;
import com.gotrid.trid.config.security.jwt.JwtService;
import com.gotrid.trid.config.security.userdetails.UserPrincipal;
import com.gotrid.trid.core.auth.refresh.RefreshToken;
import com.gotrid.trid.core.auth.refresh.RefreshTokenService;
import com.gotrid.trid.core.auth.token.Token;
import com.gotrid.trid.core.auth.token.TokenRepository;
import com.gotrid.trid.core.user.model.Gender;
import com.gotrid.trid.core.user.model.Role;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.user.repository.RoleRepository;
import com.gotrid.trid.core.user.repository.UserRepository;
import com.gotrid.trid.infrastructure.email.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gotrid.trid.core.auth.token.TokenType.ACTIVATION;
import static com.gotrid.trid.core.auth.token.TokenType.PASSWORD_RESET;
import static com.gotrid.trid.infrastructure.email.service.EmailTemplateName.ACTIVATE_ACCOUNT;
import static com.gotrid.trid.infrastructure.email.service.EmailTemplateName.RESET_PASSWORD;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TransactionTemplate transactionTemplate;
    private final RefreshTokenService refreshTokenService;
    @Value("${application.mailing.frontend.url}")
    private String Url;

    public void register(RegistrationRequest request) throws MessagingException {
        String normalizedEmail = request.email().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException("Email already registered: " + normalizedEmail);
        }

        validateAge(request.birthDate());

        var userRole = roleRepository.findByName("ROLE_USER")
                //todo - better exception handling
                .orElseThrow(() -> new IllegalStateException("Role USER was not initialized"));
        Gender gender;
        try {
            gender = Gender.valueOf(request.gender().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidGenderException("Gender must be MALE, FEMALE, or PREFER_NOT_TO_SAY");
        }

        var user = Users.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.password()))
                .accountLocked(false)
                .enabled(false)
                .roles(Set.of(userRole))
                .gender(gender)
                .dob(request.birthDate())
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void validateAge(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now().minusYears(13))) {
            throw new InvalidAgeException("You must be at least 13 years old to register.");
        }
    }

    private void sendValidationEmail(Users user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getName(),
                ACTIVATE_ACCOUNT,
                Url + "/activate-account?token=" + newToken,
                newToken
        );
    }

    private String generateAndSaveActivationToken(Users user) {
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .type(ACTIVATION)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int ri = random.nextInt(characters.length()); // 0..9
            codeBuilder.append(characters.charAt(ri));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email().toLowerCase(),
                        request.password()
                )
        );

        // Change this line to use UserPrincipal
        var userPrincipal = (UserPrincipal) auth.getPrincipal();
        var user = userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.getName());

        // Generate access token with UserPrincipal
        var jwtToken = jwtService.generateToken(claims, userPrincipal);

        refreshTokenService.revokeAllUserTokens(user);

        // Use Users entity for database operations
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .email(user.getEmail())
                .fullName(user.getName())
                .build();
    }

    public AuthenticationResponse refreshToken(String refreshTokenString) {
        return refreshTokenService.findByToken(refreshTokenString)
                .filter(refreshTokenService::isTokenValid)
                .map(refreshToken -> {
                    Users user = refreshToken.getUser();
                    UserPrincipal userPrincipal = new UserPrincipal(user);

                    var claims = new HashMap<String, Object>();
                    claims.put("fullName", user.getName());
                    String accessToken = jwtService.generateToken(claims, userPrincipal);

                    RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken);

                    return AuthenticationResponse.builder()
                            .token(accessToken)
                            .refreshToken(newRefreshToken.getToken())
                            .roles(user.getRoles().stream()
                                    .map(Role::getName)
                                    .collect(Collectors.toSet()))
                            .email(user.getEmail())
                            .fullName(user.getName())
                            .build();
                })
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
    }

    public void logout(String refreshToken) {
        refreshTokenService.revokeRefreshToken(refreshToken);
    }


    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new TokenExpiredException("Token expired! A new activation email has been sent.");
        }

        if (savedToken.getType() != ACTIVATION || savedToken.getValidatedAt() != null) {
            throw new InvalidTokenException("Invalid or already used token");
        }

        transactionTemplate.execute(ignored -> {
            var user = userRepository.findById(savedToken.getUser().getId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            user.setEnabled(true);
            userRepository.save(user);
            savedToken.setValidatedAt(LocalDateTime.now());
            tokenRepository.save(savedToken);
            return null;
        });
    }

    public void forgotPassword(String email) throws MessagingException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        String token = UUID.randomUUID().toString();
        Token resetToken = Token.builder()
                .token(token)
                .type(PASSWORD_RESET)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.save(resetToken);

        emailService.sendEmail(
                user.getEmail(),
                user.getName(),
                RESET_PASSWORD,
                Url + "/reset-password?token=" + token,
                token
        );
    }

    @Transactional
    public void resetPassword(String tokenString, String newPassword) {
        Token resetToken = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid reset token"));

        if (resetToken.getType() != PASSWORD_RESET ||
                resetToken.getExpiresAt().isBefore(LocalDateTime.now()) ||
                resetToken.getValidatedAt() != null
        ) {
            throw new InvalidTokenException("Invalid or expired reset token");
        }

        Users user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(resetToken);
    }
}
