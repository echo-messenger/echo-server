package com.gfilangeri.echo.entities;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("contacts")
public class Contact {
    @PrimaryKey
    private @NonNull String contactId;
    private @NonNull String user1Id;
    private @NonNull String user2Id;
}
