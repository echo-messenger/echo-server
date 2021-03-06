package com.gfilangeri.echo.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConversationResponse implements Comparable<ConversationResponse> {
    private String name;
    private List<String> userIds;
    private List<String> userNames;
    private String conversationId;
    private boolean group;
    private String lastMessage;
    private String lastSender;
    private Long timestamp;

    @Override
    public int compareTo(ConversationResponse other) {
        return -timestamp.compareTo(other.getTimestamp());
    }
}
