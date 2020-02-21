package com.gfilangeri.echo.entities;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("messages")
public class Message {
    @PrimaryKey
    private @NonNull String id;
    @Indexed
    private @NonNull String conversationId;
    private @NonNull String senderId;
    private @NonNull String messageContent;
    private Long timestamp;
}