package com.gfilangeri.echo.services;

import com.gfilangeri.echo.entities.Contact;
import com.gfilangeri.echo.entities.User;
import com.gfilangeri.echo.repositories.ContactRepository;
import com.gfilangeri.echo.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class ContactService {
    private ContactRepository contactRepository;
    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository, UserService userService, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public Iterable<Contact> getAllContactPairs() {
        return contactRepository.findAll();
    }

    public List<User> getAllContactsForUser(String id) {
        Iterable<Contact> allContacts = contactRepository.findAll();
        List<User> response = new ArrayList<>();
        for (Contact contact : allContacts) {
            String id1 = contact.getUser1Id();
            String id2 = contact.getUser2Id();
            if (id1.equals(id) || id2.equals(id)) {
                if (!id1.equals(id)) {
                    userService
                            .getUser(id1)
                            .ifPresent(user -> {
                                response.add(user);
                            });
                }
                if (!id2.equals(id)) {
                    userService
                            .getUser(id2)
                            .ifPresent(user -> {
                                response.add(user);
                            });
                }
            }
        }
        return response;
    }

    public Optional<Contact> getContactPair(String id) {
        return contactRepository.findById(id);
    }

    public String deleteContact(String id) {
        boolean result = contactRepository.existsById(id);
        contactRepository.deleteById(id);
        return "{ \"success\" : "+ (result ? "true" : "false") +" }";
    }

    public ResponseEntity<Contact> addContact(Contact newContact) {
        String id = String.valueOf(new Random().nextInt());
        Contact contact = new Contact();
        boolean res1 = userRepository.existsById(newContact.getUser1Id());
        boolean res2 = userRepository.existsById(newContact.getUser2Id());
        if (!res1 || !res2) {
            log.error("User does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        contact.setContactId(id);
        contact.setUser1Id(newContact.getUser1Id());
        contact.setUser2Id(newContact.getUser2Id());
        contactRepository.save(contact);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }
}

