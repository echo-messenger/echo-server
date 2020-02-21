package com.gfilangeri.echo.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    private @NonNull String id;
    private @NonNull String firstName;
    private @NonNull String lastName;
    private @NonNull String email;
    private @NonNull String plainPassword;
    private String profilePicture;
}