package com.gfilangeri.echo.controllers;

import com.gfilangeri.echo.entities.Contact;
import com.gfilangeri.echo.entities.User;
import com.gfilangeri.echo.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ContactController {
    private ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts")
    @CrossOrigin
    public Iterable<Contact> getAllContactPairs() {
        return contactService.getAllContactPairs();
    }

    @GetMapping("/contacts/{id}")
    @CrossOrigin
    public List<User> getAllContactsForUser(@PathVariable String id) {
        return contactService.getAllContactsForUser(id);
    }

    @GetMapping("/contact/{id}")
    @CrossOrigin
    public Optional<Contact> getContactPair(@PathVariable String id) {
        return contactService.getContactPair(id);
    }

    @DeleteMapping(value = "/contact/{id}", produces = "application/json; charset=utf-8")
    @CrossOrigin
    public String deleteContact(@PathVariable String id) {
        return contactService.deleteContact(id);
    }

    @PostMapping("/contact")
    @CrossOrigin
    public ResponseEntity<Contact> addContact(@RequestBody Contact newContact) {
        return contactService.addContact(newContact);
    }
}

