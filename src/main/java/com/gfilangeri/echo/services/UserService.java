package com.gfilangeri.echo.services;

import com.gfilangeri.echo.entities.User;
import com.gfilangeri.echo.repositories.UserRepository;
import com.gfilangeri.echo.requests.SignInRequest;
import com.gfilangeri.echo.requests.UserRequest;
import lombok.extern.log4j.Log4j2;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public User getUserByEmail(String email) {
        return StreamSupport
                .stream(userRepository.findAll().spliterator(), false)
                .filter(user -> email.equals(user.getEmail()))
                .findAny()
                .orElse(null);
    }

    public Optional<User> updateUser(UserRequest newUser, String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setEmail(newUser.getEmail());
            user.setHashedPassword(hashPassword(newUser.getPlainPassword()));
            userRepository.save(user);
        }
        return optionalUser;
    }

    public String deleteUser(String id) {
        boolean result = userRepository.existsById(id);
        userRepository.deleteById(id);
        return "{ \"success\" : " + (result ? "true" : "false") + " }";
    }

    public User addUser(UserRequest newUser) {
        String id = String.valueOf(new Random().nextInt());
        User user = new User();
        user.setId(id);
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setHashedPassword(hashPassword(newUser.getPlainPassword()));
        userRepository.save(user);
        return user;
    }

    public User signIn(SignInRequest request) {
        User found = getUserByEmail(request.getEmail());
        if (checkPassword(request.getPlainPassword(), found.getHashedPassword())) {
            return found;
        }
        return null;
    }

    private String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    private Boolean checkPassword(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword)) {
            System.out.println("The password matches.");
            return true;
        } else {
            System.out.println("The password does not match.");
            return false;
        }
    }
}

