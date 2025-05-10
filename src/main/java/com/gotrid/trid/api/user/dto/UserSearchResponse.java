package com.gotrid.trid.api.user.dto;

import java.util.Set;

public record UserSearchResponse(
        Integer id,
        String email,
        Set<String> roles
) {
}
