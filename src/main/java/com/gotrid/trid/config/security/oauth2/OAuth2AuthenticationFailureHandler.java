package com.gotrid.trid.config.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        //todo
        String targetUrl = String.format("https://trid-go.vercel.app/auth/oauth2/redirect?error=%s", 
                exception.getLocalizedMessage());
        
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}