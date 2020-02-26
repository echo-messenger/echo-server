package com.gfilangeri.echo.controllers;

import com.gfilangeri.echo.entities.User;
import com.gfilangeri.echo.requests.SignInRequest;
import com.gfilangeri.echo.requests.UserRequest;
import com.gfilangeri.echo.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@CrossOrigin()
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Iterable<User> getUsers() {
        log.info("Retrieving all users");
        return userService.getUsers();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @GetMapping("/userE/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PutMapping("/user/{id}")
    public Optional<User> updateUser(@RequestBody UserRequest newUser, @PathVariable String id) {
        return userService.updateUser(newUser, id);
    }

    @DeleteMapping(value = "/user/{id}", produces = "application/json; charset=utf-8")
    public String deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/user")
    public User addUser(@RequestBody UserRequest newUser) {
        return userService.addUser(newUser);
    }

    @PostMapping("/signin")
    public User signIn(@RequestBody SignInRequest request) {
        return userService.signIn(request);
    }
}


