package com.gfilangeri.echo.services;

import com.gfilangeri.echo.entities.Inbox;
import com.gfilangeri.echo.entities.User;
import com.gfilangeri.echo.repositories.ConversationRepository;
import com.gfilangeri.echo.repositories.InboxRepository;
import com.gfilangeri.echo.repositories.UserRepository;
import com.gfilangeri.echo.responses.ConversationResponse;
import com.gfilangeri.echo.requests.ExistingConversationRequest;
import com.gfilangeri.echo.responses.MessageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Service
public class InboxService {
    private InboxRepository inboxRepository;
    private ConversationRepository conversationRepository;
    private UserRepository userRepository;
    private MessageService messageService;


    @Autowired
    public InboxService(InboxRepository inboxRepository, ConversationRepository conversationRepository, UserRepository userRepository, MessageService messageService) {
        this.inboxRepository = inboxRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    public Iterable<Inbox> getInboxes() {
        return inboxRepository.findAll();
    }

    public Optional<Inbox> getInbox(String id) {
        return inboxRepository.findById(id);
    }

    public List<ConversationResponse> getInboxesForUser(String userId) {
        List<Inbox> userInboxes = StreamSupport
                .stream(inboxRepository.findAll().spliterator(), false)
                .filter(x -> x.getUserId().equals(userId))
                .collect(Collectors.toList());
        List<ConversationResponse> responses = new ArrayList<>();
        for (Inbox inbox : userInboxes) {
            List<String> users = StreamSupport
                    .stream(inboxRepository.findAll().spliterator(), false)
                    .filter(x -> x.getConversationId().equals(inbox.getConversationId()))
                    .map(Inbox::getUserId)
                    .collect(Collectors.toList());
            conversationRepository
                    .findById(inbox.getConversationId())
                    .ifPresent(conversation -> {
                        ConversationResponse response = new ConversationResponse();
                        List<MessageResponse> messages = messageService.getMessagesInConversation(conversation.getId());
                        Collections.sort(messages);
                        MessageResponse message = new MessageResponse();
                        if (!messages.isEmpty())
                            message = messages.get(messages.size() - 1);
                        if (users.size() <= 2) {
                            response.setGroup(false);
                            users.remove(userId);
                            Optional<User> user = userRepository.findById(users.get(0));
                            if (user.isPresent()) {
                                response.setName(user.get().getFirstName() + " " + user.get().getLastName());
                            }
                        } else {
                            response.setGroup(true);
                            response.setName(conversation.getName());
                            response.setUserIds(users);
                        }
                        response.setUserIds(users);
                        response.setConversationId(conversation.getId());
                        if (messages.isEmpty()) {
                            response.setLastMessage("");
                            response.setTimestamp(Long.parseLong("0"));
                            response.setLastSender("");
                        } else {
                            response.setLastMessage(message.getMessageContent());
                            response.setTimestamp(message.getTimestamp());
                            if (message.getSenderId().equals(userId)) {
                                response.setLastSender("You");
                            } else {
                                response.setLastSender(message.getSenderName());
                            }
                        }
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
