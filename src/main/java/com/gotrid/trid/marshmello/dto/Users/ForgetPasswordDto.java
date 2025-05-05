package com.gotrid.trid.marshmello.dto.Users;

import lombok.Data;

@Data
public class ForgetPasswordDto {
    private String email;
    private String newPassword;
}

