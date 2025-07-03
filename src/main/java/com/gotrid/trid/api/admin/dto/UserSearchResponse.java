package com.gotrid.trid.api.admin.dto;

import java.util.Set;

public record UserSearchResponse(
        Integer id,
        String email,
        Set<String> roles
) {
}
