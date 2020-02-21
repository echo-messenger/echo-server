package com.gfilangeri.echo.controllers;

import com.gfilangeri.echo.entities.Inbox;
import com.gfilangeri.echo.responses.ConversationResponse;
import com.gfilangeri.echo.requests.ExistingConversationRequest;
import com.gfilangeri.echo.services.InboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class InboxController {
    private InboxService inboxService;

    @Autowired
    public InboxController(InboxService inboxService) {
        this.inboxService = inboxService;
    }

    @GetMapping("/inboxes")
    @CrossOrigin
    public Iterable<Inbox> getInboxes() {
        return inboxService.getInboxes();
    }

    @GetMapping("/inbox/{id}")
    @CrossOrigin
    public Optional<Inbox> getInbox(@PathVariable String id) {
        return inboxService.getInbox(id);
    }

    @GetMapping("/inboxes/{userId}")
    @CrossOrigin
    public List<ConversationResponse> getInboxesForUser(@PathVariable String userId) {
        return inboxService.getInboxesForUser(userId);
    }

    @PutMapping("/inbox/{id}")
    @CrossOrigin
    public Optional<Inbox> updateInbox(@RequestBody Inbox newConversation, @PathVariable String id) {
        return inboxService.updateInbox(newConversation, id);
    }

    @DeleteMapping(value = "/inbox/{id}", produces = "application/json; charset=utf-8")
    @CrossOrigin
    public String deleteInbox(@PathVariable String id) {
        return inboxService.deleteInbox(id);
    }

    @PostMapping("/inbox")
    @CrossOrigin
    public ResponseEntity<Inbox> addInbox(@RequestBody Inbox newConversation) {
        return inboxService.addInbox(newConversation);
    }

    @PostMapping("/existing-conversation")
    @CrossOrigin
    public String checkIfConversationExists(@RequestBody ExistingConversationRequest request) {
        return inboxService.checkIfConversationExists(request);
    }

}
