package com.gotrid.trid.marshmello.dto.Users;
import lombok.Data;

import java.util.Collection;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private Collection<String> roles;
}





