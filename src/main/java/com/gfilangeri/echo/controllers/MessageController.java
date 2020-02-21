package com.gfilangeri.echo.controllers;

import com.gfilangeri.echo.entities.Message;
import com.gfilangeri.echo.responses.MessageResponse;
import com.gfilangeri.echo.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
public class MessageController {
    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    @CrossOrigin
    public Iterable<Message> getMessages() {
        return messageService.getMessages();
    }

    @GetMapping("/messages/{conversationId}")
    @CrossOrigin
    public List<MessageResponse> getMessagesInConversation(@PathVariable String conversationId) {
        List<MessageResponse> res = messageService.getMessagesInConversation(conversationId);
        Collections.sort(res);
        return res;
    }

    @GetMapping("/message/{id}")
    @CrossOrigin
    public Optional<Message> getMessage(@PathVariable String id) {
        return messageService.getMessage(id);
    }

    @PutMapping("/message/{id}")
    @CrossOrigin
    public Optional<Message> updateMessage(@RequestBody Message newMessage, @PathVariable String id) {
        return messageService.updateMessage(newMessage, id);
    }

    @DeleteMapping(value = "/message/{id}", produces = "application/json; charset=utf-8")
    @CrossOrigin
    public String deleteMessage(@PathVariable String id) {
        return messageService.deleteMessage(id);
    }

    @PostMapping("/message")
    @CrossOrigin
    public MessageResponse addMessage(@RequestBody Message newMessage) {
        return messageService.addMessage(newMessage);
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public MessageResponse greeting(Message message) {
        return addMessage(message);
    }
}
