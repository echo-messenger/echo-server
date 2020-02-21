package com.gfilangeri.echo.requests;

import lombok.Data;

import java.util.Set;

@Data
public class ExistingConversationRequest {
    private Set<String> userIds;
}