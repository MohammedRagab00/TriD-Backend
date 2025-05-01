package com.gotrid.trid.auth;

import com.gotrid.trid.auth.RefreshToken.RefreshToken;
import com.gotrid.trid.auth.RefreshToken.RefreshTokenService;
import com.gotrid.trid.auth.dto.AuthenticationRequest;
import com.gotrid.trid.auth.dto.AuthenticationResponse;
import com.gotrid.trid.auth.dto.RegistrationRequest;
import com.gotrid.trid.email.EmailService;
import com.gotrid.trid.exception.EmailAlreadyExistsException;
import com.gotrid.trid.exception.InvalidRefreshTokenException;
import com.gotrid.trid.exception.InvalidTokenException;
import com.gotrid.trid.exception.TokenExpiredException;
import com.gotrid.trid.role.RoleRepository;
import com.gotrid.trid.security.JwtService;
import com.gotrid.trid.user.Users;
import com.gotrid.trid.user.UsersRepository;
import com.gotrid.trid.user.token.Token;
import com.gotrid.trid.user.token.TokenRepository;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import static com.gotrid.trid.email.EmailTemplateName.ACTIVATE_ACCOUNT;
import static com.gotrid.trid.email.EmailTemplateName.RESET_PASSWORD;
import static com.gotrid.trid.user.token.TokenType.ACTIVATION;
import static com.gotrid.trid.user.token.TokenType.PASSWORD_RESET;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository userRepository;
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

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered: " + normalizedEmail);
        }


        var userRole = roleRepository.findByName("ROLE_USER")
                //todo - better exception handling
                .orElseThrow(() -> new IllegalStateException("Role USER was not initialized"));

        var user = Users.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.password()))
                .accountLocked(false)
                .enabled(false)
                .roles(Set.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
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
        var claims = new HashMap<String, Object>();
        var user = (Users) auth.getPrincipal();
        claims.put("fullName", user.getName());

        // Generate an access token
        var jwtToken = jwtService.generateToken(claims, user);

        refreshTokenService.revokeAllUserTokens(user);

        // Generate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthenticationResponse refreshToken(String refreshTokenString) {
        return refreshTokenService.findByToken(refreshTokenString)
                .filter(refreshTokenService::isTokenValid)
                .map(refreshToken -> {
                    Users user = refreshToken.getUser();

                    // Generate a new access token
                    var claims = new HashMap<String, Object>();
                    claims.put("fullName", user.getName());
                    String accessToken = jwtService.generateToken(claims, user);

                    // Generate a new refresh token (token rotation)
                    RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken);

                    return AuthenticationResponse.builder()
                            .token(accessToken)
                            .refreshToken(newRefreshToken.getToken()) // Reuse the same refresh token
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

        transactionTemplate.execute(_ -> {
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
                user.getUsername(),
                RESET_PASSWORD,
                Url + "/reset-password?token=" + token,
                token
        );
    }

    @Transactional
    public void resetPassword(String tokenString, String newPassword) {
        Token resetToken = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new InvalidTokenException("Invalid reset token"));

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
