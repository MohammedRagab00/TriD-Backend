package com.gotrid.trid.marshmello.dto.Users;


import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
