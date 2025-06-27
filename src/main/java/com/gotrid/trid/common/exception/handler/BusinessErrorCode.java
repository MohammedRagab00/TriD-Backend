package com.gotrid.trid.common.exception.handler;

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
    INVALID_JWT_SIGNATURE(1008, UNAUTHORIZED, "Invalid JWT signature"),

    // User Management
    EMAIL_ALREADY_EXISTS(2001, CONFLICT, "Email address already registered"),
    INCORRECT_PASSWORD(2002, BAD_REQUEST, "Incorrect password"),
    NEW_PASSWORD_MISMATCH(2003, BAD_REQUEST, "New passwords do not match"),
    INVALID_PASSWORD(2004, BAD_REQUEST, "Invalid password"),
    USER_NOT_FOUND(2005, NOT_FOUND, "User not found"),

    // File Operations
    FILE_VALIDATION_ERROR(3001, BAD_REQUEST, "File validation failed"),
    FILE_UPLOAD_ERROR(3002, INTERNAL_SERVER_ERROR, "Failed to upload file"),
    FILE_DELETE_ERROR(3003, INTERNAL_SERVER_ERROR, "Failed to delete file"),

    // General
    VALIDATION_ERROR(4001, BAD_REQUEST, "Validation failed"),
    INVALID_ARGUMENT(4002, BAD_REQUEST, "Invalid argument provided"),
    DUPLICATE_RESOURCE(4003, CONFLICT, "Resource already exists"),
    INTERNAL_ERROR(5001, INTERNAL_SERVER_ERROR, "Internal server error"),

    // Shop Management
    SHOP_NAME_EXISTS(6001, CONFLICT, "Shop name already exists"),
    UNAUTHORIZED_ACCESS(6002, FORBIDDEN, "Unauthorized"),
    SHOP_ALREADY_EXISTS(6003, CONFLICT, "You already have a shop. Only one shop per user is allowed"),
    INVALID_SOCIAL_LINK(6004, BAD_REQUEST, "Invalid social media link"),
    SHOP_ASSET_ERROR(6005, BAD_REQUEST, "Shop asset error"),

    ENTITY_NOT_FOUND(7001, NOT_FOUND, "Requested entity not found"),
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String description;
}
