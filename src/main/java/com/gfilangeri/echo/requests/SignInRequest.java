package com.gfilangeri.echo.requests;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String plainPassword;
}
