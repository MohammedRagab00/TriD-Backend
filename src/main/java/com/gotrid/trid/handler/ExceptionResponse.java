package com.gotrid.trid.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Map;
import java.util.Set;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ExceptionResponse(
        Integer code,
        String message,
        String details,
        Set<String> validationErrors,
        Map<String, String> errors
) {
}