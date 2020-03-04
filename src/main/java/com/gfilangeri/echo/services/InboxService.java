package com.gfilangeri.echo.services;

import com.gfilangeri.echo.entities.Conversation;
import com.gfilangeri.echo.entities.Inbox;
import com.gfilangeri.echo.entities.Message;
import com.gfilangeri.echo.entities.User;
import com.gfilangeri.echo.repositories.ConversationRepository;
import com.gfilangeri.echo.repositories.InboxRepository;
import com.gfilangeri.echo.repositories.MessageRepository;
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

@Log4j2
@Service
public class InboxService {
    private InboxRepository inboxRepository;
    private ConversationRepository conversationRepository;
    private UserRepository userRepository;
    private MessageService messageService;
    private MessageRepository messageRepository;


    @Autowired
    public InboxService(InboxRepository inboxRepository, ConversationRepository conversationRepository,
                        UserRepository userRepository, MessageService messageService,
                        MessageRepository messageRepository) {
        this.inboxRepository = inboxRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.messageRepository = messageRepository;
    }

    public Iterable<Inbox> getInboxes() {
        return inboxRepository.findAll();
    }

    public Optional<Inbox> getInbox(String id) {
        return inboxRepository.findById(id);
    }

    public List<ConversationResponse> getInboxesForUser(String userId) {
        List<Message> messages = messageRepository.findAll();
        List<ConversationResponse> responses = new ArrayList<>();
        List<Inbox> allInboxes = inboxRepository.findAll();
        List<String> userConversationIds = allInboxes
                .stream().filter(inbox -> inbox.getUserId().equals(userId))
                .map(Inbox::getConversationId).collect(Collectors.toList());

        List<Conversation> userConversations = userConversationIds.stream()
                .map(id -> conversationRepository.findById(id).get()).collect(Collectors.toList());

        for (Conversation conv : userConversations) {
            ConversationResponse response = new ConversationResponse();
            List<String> userIds = allInboxes.stream().filter(inbox -> inbox.getConversationId().equals(conv.getId()))
                    .map(Inbox::getUserId).collect(Collectors.toList());
            MessageResponse lastMessage = messageService.getLastMessageInConversation(conv.getId(), messages);

            response.setLastMessage(lastMessage.getMessageContent());
            response.setTimestamp(lastMessage.getTimestamp());
            String lastSender = lastMessage.getSenderId().equals(userId) ? "You" : lastMessage.getSenderName();
            response.setLastSender(lastSender);

            if (userIds.size() <= 2) {
                response.setGroup(false);
                userIds.remove(userId);
                Optional<User> user = userRepository.findById(userIds.get(0));
                if (user.isPresent()) response.setName(user.get().getFirstName() + " " + user.get().getLastName());
            } else {
                response.setGroup(true);
                response.setName(conv.getName());
                response.setUserIds(userIds);
            }
            response.setUserIds(userIds);
            response.setConversationId(conv.getId());
            responses.add(response);
        }
        Collections.sort(responses);
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
