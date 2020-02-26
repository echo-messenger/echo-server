package com.gfilangeri.echo.entities;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table("inboxes")
public class Inbox {
    @PrimaryKey
    private @NonNull String id;
    // TODO   @Indexed
    private @NonNull String userId;
    // TODO   @Indexed
    private @NonNull String conversationId;
}