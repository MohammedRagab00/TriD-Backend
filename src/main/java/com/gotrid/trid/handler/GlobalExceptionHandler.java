package com.gotrid.trid.handler;

import com.gotrid.trid.exception.EmailAlreadyExistsException;
import com.gotrid.trid.exception.InvalidTokenException;
import com.gotrid.trid.exception.InvalidRefreshTokenException;
import com.gotrid.trid.exception.TokenExpiredException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.gotrid.trid.handler.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(
            LockedException exp
    ) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .code(ACCOUNT_LOCKED.getCode())
                        .message(exp.getMessage())
                        .details(ACCOUNT_LOCKED.getDescription())
                        .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(
            DisabledException exp
    ) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .code(ACCOUNT_DISABLED.getCode())
                        .message(exp.getMessage())
                        .details(ACCOUNT_DISABLED.getDescription())
                        .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(
            BadCredentialsException exp
    ) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .code(BAD_CREDENTIALS.getCode())
                        .message(exp.getMessage())
                        .details(BAD_CREDENTIALS.getDescription())
                        .build()
                );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleException(
            InvalidTokenException exp
    ) {
        return ResponseEntity
                .status(INVALID_TOKEN.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .code(INVALID_TOKEN.getCode())
                        .message(INVALID_TOKEN.getDescription())
                        .details(exp.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(
            MessagingException exp
    ) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .message(exp.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(
            MethodArgumentNotValidException exp
    ) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors()
                .forEach(err -> {
                    var errorMessage = err.getDefaultMessage();
                    if (errorMessage != null) {
                        errors.add(errorMessage);
                    }
                });
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build()
                );
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleException(
            TokenExpiredException exp
    ) {
        return ResponseEntity
                .status(GONE)
                .body(ExceptionResponse.builder()
                        .message(exp.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleException(
            AuthorizationDeniedException exp
    ) {
        return ResponseEntity
                .status(CONFLICT)
                .body(ExceptionResponse.builder()
                        .code(AUTHORIZATION_DENIED.getCode())
                        .message(AUTHORIZATION_DENIED.getDescription())
                        .details("You don't have sufficient permissions to access this resource")
                        .build()
                );
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ExceptionResponse> handleException(
            InvalidRefreshTokenException ex) {

        ExceptionResponse response = ExceptionResponse.builder()
                .code(UNAUTHORIZED.value())
                .message("Authentication failed")
                .details(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, UNAUTHORIZED);
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleException(
            EmailAlreadyExistsException exp
    ) {
        log.warn("Email already exists: {}", exp.getMessage());
        return ResponseEntity
                .status(CONFLICT)
                .body(ExceptionResponse.builder()
                        .code(EMAIL_ALREADY_EXISTS.getCode())
                        .message(EMAIL_ALREADY_EXISTS.getDescription())
                        .details("The email address is already in use by another account")
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(
            Exception exp
    ) {
        //todo - log the exception (partially done)
        log.error("Unhandled exception occurred: {}", exp.getMessage(), exp);

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .message("Internal error, contact the admin")
                        .details(exp.getMessage())
                        .build()
                );
    }

}