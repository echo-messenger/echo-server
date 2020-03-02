package com.gfilangeri.echo.entities;

import com.gfilangeri.echo.responses.MessageResponse;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("users")
public class User implements Comparable<User>{
    @PrimaryKey
    private @NonNull String id;
    private @NonNull String firstName;
    private @NonNull String lastName;
    private @NonNull String email;
    private @NonNull String hashedPassword;
    private String profilePicture;

    @Override
    public int compareTo(User other) {
        return firstName.compareTo(other.getFirstName());
    }
}