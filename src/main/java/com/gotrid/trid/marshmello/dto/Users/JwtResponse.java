package com.gotrid.trid.marshmello.dto.Users;

import lombok.Data;

import java.util.Collection;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private Collection<String> roles;

    public JwtResponse(String token, Long id, String username, String email, Collection<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}