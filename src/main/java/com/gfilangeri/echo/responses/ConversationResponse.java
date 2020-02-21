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
public class ConversationResponse {
    private String name;
    private List<String> userIds;
    private List<String> userNames;
    private String conversationId;
}
