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
public class ConversationService {
    private ConversationRepository conversationRepository;
    private MessageRepository messageRepository;
    private InboxRepository inboxRepository;
    private UserRepository userRepository;
    private MessageService messageService;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository,
                               MessageRepository messageRepository,
                               InboxRepository inboxRepository,
                               UserRepository userRepository,
                               MessageService messageService) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.inboxRepository = inboxRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    public Iterable<Conversation> getConversations() {
        return conversationRepository.findAll();
    }

    public ConversationResponse getConversation(String id) {
        Optional<Conversation> res = conversationRepository.findById(id);
        List<MessageResponse> messages = messageService.getMessagesInConversation(id);
        Collections.sort(messages);
        MessageResponse message = messages.get(messages.size() - 1);
        if (res.isPresent()) {
            ConversationResponse response = new ConversationResponse();
            List<String> userIds = new ArrayList<>();
            List<String> userNames = new ArrayList<>();
            Conversation convo = res.get();
            List<Inbox> inboxes = StreamSupport
                    .stream(inboxRepository.findAll().spliterator(), false)
                    .filter(x -> x.getConversationId().equals(convo.getId()))
                    .collect(Collectors.toList());
            for (Inbox inbox : inboxes) {
                Optional<User> user = userRepository.findById(inbox.getUserId());
                userIds.add(inbox.getUserId());
                if (user.isPresent()) {
                    userNames.add(user.get().getFirstName() + " " + user.get().getLastName());
                }
            }
            if (userNames.size() > 2) response.setGroup(true);
            else response.setGroup(false);
            response.setConversationId(convo.getId());
            response.setName(convo.getName());
            response.setUserIds(userIds);
            response.setUserNames(userNames);
            response.setLastMessage(message.getMessageContent());
            response.setTimestamp(message.getTimestamp());
            return response;
        }
        return null;
    }

    public List<Message> getMessagesInConversation(String id) {
        Iterable<Message> allMessages = messageRepository.findAll();
        List<Message> messagesInConversation = new ArrayList<>();
        for (Message message : allMessages) {
            if (message.getConversationId().equals(id)) {
                messagesInConversation.add(message);
            }
        }
        return messagesInConversation;
    }

    public String deleteConversation(String id) {
        boolean result = conversationRepository.existsById(id);
        conversationRepository.deleteById(id);
        return "{ \"success\" : "+ (result ? "true" : "false") +" }";
    }

    public Optional<Conversation> updateConversation(Conversation newConversation, String id)
    {
        Optional<Conversation> optionalConversation = conversationRepository.findById(id);
        if (optionalConversation.isPresent()) {
            Conversation conv = optionalConversation.get();
            conv.setId(newConversation.getId());
            conv.setName(newConversation.getName());
            conversationRepository.save(conv);
        }
        return optionalConversation;
    }

    public ResponseEntity<Conversation> addConversation(Conversation newConversation) {
        String id = String.valueOf(new Random().nextInt());
        Conversation conversation = new Conversation();
        conversation.setId(id);
        conversation.setName(newConversation.getName());
        conversationRepository.save(conversation);
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }
}
