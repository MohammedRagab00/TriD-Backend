package com.gotrid.trid.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotrid.trid.exception.handler.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.gotrid.trid.exception.handler.BusinessErrorCodes.TOKEN_EXPIRED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            ExceptionResponse errorResponse = ExceptionResponse.builder()
                    .code(TOKEN_EXPIRED.getCode())
                    .message(TOKEN_EXPIRED.getDescription())
                    .details("Your session has expired. Please use the refresh token to get a new one.")
                    .build();

            response.setStatus(UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            objectMapper.writeValue(response.getWriter(), errorResponse);
        }

    }
}
