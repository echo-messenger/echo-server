package com.gfilangeri.echo.controllers;

import com.gfilangeri.echo.entities.Conversation;
import com.gfilangeri.echo.entities.Message;
import com.gfilangeri.echo.responses.ConversationResponse;
import com.gfilangeri.echo.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ConversationController {
    private ConversationService conversationService;

    @Autowired
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping("/conversations")
    @CrossOrigin
    public Iterable<Conversation> getConversations() {
        return conversationService.getConversations();
    }

    @GetMapping("/conversation/{id}")
    @CrossOrigin
    public ConversationResponse getConversation(@PathVariable String id) {
        return conversationService.getConversation(id);
    }

    @GetMapping("/conversation/{id}/messages")
    @CrossOrigin
    public List<Message> getMessagesInConversation(@PathVariable String id) {
        return conversationService.getMessagesInConversation(id);
    }

    @PutMapping("/conversation/{id}")
    @CrossOrigin
    public Optional<Conversation> updateConversation(@RequestBody Conversation newConversation, @PathVariable String id) {
        return conversationService.updateConversation(newConversation, id);
    }

    @DeleteMapping(value = "/conversation/{id}", produces = "application/json; charset=utf-8")
    @CrossOrigin
    public String deleteConversation(@PathVariable String id) {
        return conversationService.deleteConversation(id);
    }

    @PostMapping("/conversation")
    @CrossOrigin
    public ResponseEntity<Conversation> addConversation(@RequestBody Conversation newConversation) {
        return conversationService.addConversation(newConversation);
    }

}

