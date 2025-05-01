package com.gotrid.trid.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_PASSWORD(300, BAD_REQUEST, "Incorrect Password"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "New Password Does Not Match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "Account Locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "Account Disabled"),
    BAD_CREDENTIALS(304, BAD_REQUEST, "The email or password provided is incorrect. Please try again."),
    EMAIL_ALREADY_EXISTS(305, CONFLICT, "Email already registered"),
    TOKEN_EXPIRED(310, UNAUTHORIZED, "Token Expired"),
    AUTHORIZATION_DENIED(403, FORBIDDEN, "Access Denied"),
    INVALID_ACTIVATION_TOKEN(1008, BAD_REQUEST, "Invalid activation token"),
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String description;
}
