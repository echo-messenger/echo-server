package com.gfilangeri.echo.entities;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table("users")
public class User {
    @PrimaryKey
    private @NonNull String id;
    private @NonNull String firstName;
    private @NonNull String lastName;
    @Indexed
    private @NonNull String email;
    private @NonNull String hashedPassword;
    private String profilePicture;
}