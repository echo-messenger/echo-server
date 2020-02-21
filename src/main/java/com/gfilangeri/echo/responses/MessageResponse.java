package com.gfilangeri.echo.responses;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageResponse implements Comparable<MessageResponse> {
    private @NonNull String id;
    private @NonNull String conversationId;
    private @NonNull String senderId;
    private @NonNull String senderName;
    private @NonNull String messageContent;
    private Long timestamp;

    @Override
    public int compareTo(MessageResponse other) {
        return timestamp.compareTo(other.getTimestamp());
    }
}