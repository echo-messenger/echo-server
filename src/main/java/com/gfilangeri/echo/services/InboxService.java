package com.gfilangeri.echo.services;

import com.gfilangeri.echo.entities.Inbox;
import com.gfilangeri.echo.repositories.ConversationRepository;
import com.gfilangeri.echo.repositories.InboxRepository;
import com.gfilangeri.echo.responses.ConversationResponse;
import com.gfilangeri.echo.requests.ExistingConversationRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class InboxService {
    private InboxRepository inboxRepository;
    private ConversationRepository conversationRepository;

    @Autowired
    public InboxService(InboxRepository inboxRepository, ConversationRepository conversationRepository) {
        this.inboxRepository = inboxRepository;
        this.conversationRepository = conversationRepository;
    }

    public Iterable<Inbox> getInboxes() {
        return inboxRepository.findAll();
    }

    public Optional<Inbox> getInbox(String id) {
        return inboxRepository.findById(id);
    }

    public List<ConversationResponse> getInboxesForUser(String userId) {
        List<Inbox> userInboxes = inboxRepository.findAllByUserId(userId);
        List<ConversationResponse> responses = new ArrayList<>();
        for (Inbox inbox : userInboxes) {
            List<String> users = inboxRepository.findAllByConversationIdEquals(inbox.getConversationId())
                    .stream()
                    .map(Inbox::getUserId)
                    .collect(Collectors.toList());
            conversationRepository
                    .findById(inbox.getConversationId())
                    .ifPresent(conversation -> {
                        ConversationResponse response = new ConversationResponse();
                        response.setName(conversation.getName());
                        response.setUserIds(users);
                        response.setConversationId(conversation.getId());
                        responses.add(response);
                    });
        }
        return responses;
    }

    public String deleteInbox(String id) {
        boolean result = inboxRepository.existsById(id);
        inboxRepository.deleteById(id);
        return "{ \"success\" : " + (result ? "true" : "false") + " }";
    }

    public Optional<Inbox> updateInbox(Inbox newInbox, String id) {
        Optional<Inbox> optionalInbox = inboxRepository.findById(id);
        if (optionalInbox.isPresent()) {
            Inbox inbox = optionalInbox.get();
            inbox.setId(newInbox.getId());
            inbox.setConversationId(newInbox.getConversationId());
            inbox.setUserId(newInbox.getUserId());
            inboxRepository.save(inbox);
        }
        return optionalInbox;
    }

    public ResponseEntity<Inbox> addInbox(Inbox newInbox) {
        String id = String.valueOf(new Random().nextInt());
        Inbox inbox = new Inbox();
        inbox.setId(id);
        inbox.setConversationId(newInbox.getConversationId());
        inbox.setUserId(newInbox.getUserId());
        inboxRepository.save(inbox);
        return new ResponseEntity<>(inbox, HttpStatus.OK);
    }

    public String checkIfConversationExists(ExistingConversationRequest request) {
        List<Inbox> allInbox = inboxRepository.findAll();
        Map<String, Set<String>> groupedByConversation = new HashMap<>();
        for (Inbox inbox : allInbox) {
            String convId = inbox.getConversationId();
            String userId = inbox.getUserId();
            if (groupedByConversation.containsKey(convId)) {
                groupedByConversation.get(convId).add(userId);
                if (groupedByConversation.get(convId).equals(request.getUserIds())) {
                    return convId;
                }
            } else {
                groupedByConversation.put(convId, new HashSet<>());
                groupedByConversation.get(convId).add(userId);
                if (groupedByConversation.get(convId).equals(request.getUserIds())) {
                    return convId;
                }
            }
        }
        return null;
    }
}
