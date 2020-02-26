package com.gfilangeri.echo.services;

import com.gfilangeri.echo.entities.Message;
import com.gfilangeri.echo.entities.User;
import com.gfilangeri.echo.repositories.MessageRepository;
import com.gfilangeri.echo.responses.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private UserService userService;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public Iterable<Message> getMessages() {
        return messageRepository.findAll();
    }

    public List<MessageResponse> getMessagesInConversation(String conversationId)
    {
        List<Message> messages = StreamSupport
                .stream(messageRepository.findAll().spliterator(), false)
                .filter(x -> x.getConversationId().equals(conversationId))
                .collect(Collectors.toList());
        List<MessageResponse> messageResponses = new ArrayList<>();
        for (Message message : messages) {
            Optional<User> user = userService.getUser(message.getSenderId());
            if (user.isPresent()) {
                String name = user.get().getFirstName() + " " + user.get().getLastName();
                MessageResponse response = new MessageResponse();
                response.setId(message.getId());
                response.setConversationId(message.getConversationId());
                response.setSenderId(message.getSenderId());
                response.setSenderName(name);
                response.setMessageContent(message.getMessageContent());
                response.setTimestamp(message.getTimestamp());
                messageResponses.add(response);
            }
        }
        return messageResponses;
    }

    public Optional<Message> getMessage(String id)
    {
        return messageRepository.findById(id);
    }

    public Optional<Message> updateMessage(Message newMessage, String id)
    {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setId(newMessage.getId());
            message.setConversationId(newMessage.getConversationId());
            message.setSenderId(newMessage.getSenderId());
            message.setMessageContent(newMessage.getMessageContent());
            message.setTimestamp(newMessage.getTimestamp());
            messageRepository.save(message);
        }
        return optionalMessage;
    }

    public String deleteMessage(String id) {
        boolean result = messageRepository.existsById(id);
        messageRepository.deleteById(id);
        return "{ \"success\" : "+ (result ? "true" : "false") +" }";
    }

    public MessageResponse addMessage(Message newMessage)
    {
        String id = String.valueOf(new Random().nextInt());
        Message message = new Message();
        MessageResponse res = new MessageResponse();
        message.setId(id);
        message.setConversationId(newMessage.getConversationId());
        message.setSenderId(newMessage.getSenderId());
        message.setMessageContent(newMessage.getMessageContent());
        message.setTimestamp(newMessage.getTimestamp());
        messageRepository.save(message);

        Optional<User> user = userService.getUser(message.getSenderId());
        if (user.isPresent()) {
            String name = user.get().getFirstName() + " " + user.get().getLastName();
            res.setId(id);
            res.setConversationId(message.getConversationId());
            res.setSenderId(message.getSenderId());
            res.setSenderName(name);
            res.setMessageContent(message.getMessageContent());
        }
        return res;
    }
}
