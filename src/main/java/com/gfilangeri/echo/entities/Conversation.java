package com.gfilangeri.echo.entities;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table("conversations")
public class Conversation {
    @PrimaryKey
    private @NonNull String id;
    private @NonNull String name;
}