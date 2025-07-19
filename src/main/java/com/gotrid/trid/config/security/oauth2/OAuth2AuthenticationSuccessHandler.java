package com.gotrid.trid.config.security.oauth2;

import com.gotrid.trid.config.security.jwt.JwtService;
import com.gotrid.trid.core.user.model.Gender;
import com.gotrid.trid.core.user.model.Role;
import com.gotrid.trid.core.user.model.Users;
import com.gotrid.trid.core.user.repository.IRoleRepository;
import com.gotrid.trid.core.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Qualifier("jpa")
    private final IRoleRepository roleRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        //* To get the provider name
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String provider = authToken.getAuthorizedClientRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        ExtractedData extractedData = extractAll(attributes);
        String email = extractedData.email().toLowerCase();
        if (!userRepository.existsByEmail(email))
            saveUserToDatabase(extractedData);
        CustomOAuth2User customUser = new CustomOAuth2User(email);

        //* Generate JWT token
        String fullName = extractedData.firstName() + " " + extractedData.lastName();
        String jwtToken = jwtService.generateToken(Map.of("fullName", fullName), customUser);

        String targetUrl = String.format("https://go-trid.vercel.app/auth/oauth2/redirect?token=%s&email=%s&name=%s&provider=%s",
                jwtToken,
                extractedData.email(),
                fullName,
                provider
        );

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private ExtractedData extractAll(Map<String, Object> attributes) {
        return new ExtractedData(
                (String) attributes.get("email"),
                (String) attributes.get("given_name"),
                (String) attributes.get("family_name")
        );
    }

    private void saveUserToDatabase(ExtractedData dto) {
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        Users newUser = Users.builder()
                .firstname(dto.firstName())
                .lastname(dto.lastName())
                .email(dto.email().toLowerCase())
                .roles(Set.of(role))
                .accountLocked(false)
                .enabled(true)
                .gender(Gender.PREFER_NOT_TO_SAY)
                .build();
        userRepository.save(newUser);
    }
}