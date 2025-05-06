package com.gotrid.trid.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum BusinessErrorCode {
    // Authentication & Authorization
    BAD_CREDENTIALS(1001, UNAUTHORIZED, "The email or password provided is incorrect"),
    ACCOUNT_LOCKED(1002, FORBIDDEN, "Account is locked"),
    ACCOUNT_DISABLED(1003, FORBIDDEN, "Account is disabled"),
    AUTHORIZATION_DENIED(1004, FORBIDDEN, "Access denied"),
    TOKEN_EXPIRED(1005, UNAUTHORIZED, "Token has expired"),
    INVALID_TOKEN(1006, UNAUTHORIZED, "Invalid token"),
    INVALID_REFRESH_TOKEN(1007, UNAUTHORIZED, "Invalid refresh token"),

    // User Management
    EMAIL_ALREADY_EXISTS(2001, CONFLICT, "Email address already registered"),
    INCORRECT_PASSWORD(2002, BAD_REQUEST, "Incorrect password"),
    NEW_PASSWORD_MISMATCH(2003, BAD_REQUEST, "New passwords do not match"),

    // File Operations
    FILE_VALIDATION_ERROR(3001, BAD_REQUEST, "File validation failed"),
    FILE_UPLOAD_ERROR(3002, INTERNAL_SERVER_ERROR, "Failed to upload file"),
    FILE_DELETE_ERROR(3003, INTERNAL_SERVER_ERROR, "Failed to delete file"),

    // General
    VALIDATION_ERROR(4001, BAD_REQUEST, "Validation failed"),
    INTERNAL_ERROR(5001, INTERNAL_SERVER_ERROR, "Internal server error"),

    // Shop Management
    SHOP_NOT_FOUND(6001, NOT_FOUND, "Shop not found"),
    SHOP_NAME_EXISTS(6002, CONFLICT, "Shop name already exists"),
    UNAUTHORIZED_ACCESS(6003, FORBIDDEN, "Unauthorized shop access"),
    INVALID_SOCIAL_LINK(6005, BAD_REQUEST, "Invalid social media link"),
    SHOP_ASSET_ERROR(6006, BAD_REQUEST, "Shop asset error"),
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String description;
}
