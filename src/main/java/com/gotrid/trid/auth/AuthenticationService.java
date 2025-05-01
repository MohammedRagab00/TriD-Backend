package com.gotrid.trid.auth;

import com.gotrid.trid.auth.RefreshToken.RefreshToken;
import com.gotrid.trid.auth.RefreshToken.RefreshTokenService;
import com.gotrid.trid.auth.dto.AuthenticationRequest;
import com.gotrid.trid.auth.dto.AuthenticationResponse;
import com.gotrid.trid.auth.dto.RegistrationRequest;
import com.gotrid.trid.email.EmailService;
import com.gotrid.trid.email.EmailTemplateName;
import com.gotrid.trid.exception.EmailAlreadyExistsException;
import com.gotrid.trid.exception.InvalidActivationTokenException;
import com.gotrid.trid.exception.InvalidRefreshTokenException;
import com.gotrid.trid.exception.TokenExpiredException;
import com.gotrid.trid.role.RoleRepository;
import com.gotrid.trid.security.JwtService;
import com.gotrid.trid.user.Token;
import com.gotrid.trid.user.TokenRepository;
import com.gotrid.trid.user.Users;
import com.gotrid.trid.user.UsersRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TransactionTemplate transactionTemplate;
    private final RefreshTokenService refreshTokenService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        String normalizedEmail = request.email().toLowerCase();

        if (usersRepository.findByEmail(normalizedEmail).isPresent()) {
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
        usersRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(Users user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken
        );
    }

    private String generateAndSaveActivationToken(Users user) {
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
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


    //    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidActivationTokenException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new TokenExpiredException("Token expired! A new activation email has been sent.");
        }

        transactionTemplate.execute(_ -> {
            var user = usersRepository.findById(savedToken.getUser().getId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            user.setEnabled(true);
            usersRepository.save(user);
            savedToken.setValidatedAt(LocalDateTime.now());
            tokenRepository.save(savedToken);
            return null;
        });

    }
}
